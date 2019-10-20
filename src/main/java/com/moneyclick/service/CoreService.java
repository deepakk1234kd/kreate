package com.moneyclick.service;

import com.moneyclick.bo.IdentificationBo;
import com.moneyclick.bo.PersonBo;
import com.moneyclick.repository.PersonIdentificationRepository;
import com.moneyclick.utils.Constants;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CoreService {
    @Autowired
    private PersonIdentificationRepository personIdentificationRepository;

    @Autowired
    private CloudmersiveService cloudmersiveService;

    @Autowired
    private PersonService personService;

    public void process() {
        try (Stream<Path> paths = Files.walk(Paths.get(Constants.INPUT_DIR), 1)) {
            paths.filter(Files::isDirectory)
                    .filter(p -> !p.toString().equals(Constants.INPUT_DIR))
                    .map(this::processPerson)
                    .forEach(personService::savePersonDetails);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private PersonBo processPerson(Path personDir) {
        try (Stream<Path> personFiles = Files.walk(personDir, 1)) {
            List<IdentificationBo> identities = personFiles.filter(Files::isRegularFile)
                    .filter(path -> {
                        String fileName = FilenameUtils.removeExtension(path.getFileName().toString());
                        return Constants.DOC_TYPES.contains(fileName.toUpperCase());
                    })
                    .map(imagePath -> {
                        String fileName = FilenameUtils.removeExtension(imagePath.getFileName().toString());
                        List<String> textLines = cloudmersiveService.processImage(imagePath);
                        if (textLines.isEmpty())
                            return null;
                        switch (fileName.toUpperCase()) {
                            case Constants.AADHAR:
                                return processAadhar(textLines);
                            case Constants.PAN:
                                return processPAN(textLines);
                            default:
                                return null;
                        }
                    })
                    .collect(Collectors.toList());

            Map<String, IdentificationBo> idMap = new HashMap<>();
            identities.forEach(id -> idMap.put(id.getIdType(), id));

            return new PersonBo(idMap);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Pattern AADHAR_DOB = Pattern.compile(".*DOB:\\s?(\\d\\d/\\d\\d/\\d{4}).*", Pattern.CASE_INSENSITIVE);
    private Pattern AADHAR_YEAR = Pattern.compile(".*Year of Birth:\\s?(\\d{4}).*", Pattern.CASE_INSENSITIVE);
    private Pattern PAN_DOB = Pattern.compile(".*(\\d\\d/\\d\\d/\\d{4}).*");

    private int getDateFieldIndex(List<String> textLines) {
        int i = 0;
        for (String text : textLines) {
            Matcher m = PAN_DOB.matcher(text);
            if (m.matches())
                return i;
            i++;
        }

        return -1;
    }

    private IdentificationBo processAadhar(List<String> textLines) {
        int dateIndex = 0;
        for (String text : textLines) {
            if (AADHAR_DOB.matcher(text).matches() || AADHAR_YEAR.matcher(text).matches())
                break;
            dateIndex++;
        }

        String name = textLines.get(dateIndex-1).trim();
        String dobStr = textLines.get(dateIndex).trim();
        String id = textLines.get(dateIndex+2).trim();

        Matcher matcher = AADHAR_YEAR.matcher(dobStr);
        String dob = null;
        if (matcher.matches()) {
            try {
                dob = matcher.group(1).trim();
            } catch (Exception e) {
                //System.out.println("Group 2 mismatch");
                //System.out.println(e.getMessage());
            }
        }

        if (dob == null) {
            matcher = AADHAR_DOB.matcher(dobStr);
            if (matcher.matches()) {
                try {
                    dob = convertDate(matcher.group(1));
                } catch (Exception e) {
//                    System.out.println("Group 1 mismatch");
//                    System.out.println(e.getMessage());
                }
            }
        }

        if (dob == null){
            System.out.println("Invalid Document");
        }

        return new IdentificationBo(id, Constants.AADHAR, name, dob);
    }

    private IdentificationBo processPAN(List<String> textLines) {
        int dateIndex = 0;
        for (String text : textLines) {
            if (PAN_DOB.matcher(text).matches())
                break;
            dateIndex++;
        }

        String name = textLines.get(dateIndex-2).trim();
        String dob = convertDate(textLines.get(dateIndex));
        String id = textLines.get(dateIndex+2).trim();

        return new IdentificationBo(id, Constants.PAN, name, dob);
    }

    private String convertDate(String date) {
        List<String> dateParts = Arrays.asList(date.trim().split("/"));
        Collections.reverse(dateParts);
        return String.join("-", dateParts);
    }
}
