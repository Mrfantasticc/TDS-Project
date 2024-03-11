package com.tds.controller;


import com.tds.userDetail.FileUploadForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.tds.helper.Helper.*;

//@RestController
@Controller
@CrossOrigin("*")
public class Controllers {

    @GetMapping("/TDS")
    public String showUploadForm(Model model) {
        System.out.println(".......1.........");
        model.addAttribute("fileUploadForm", new FileUploadForm());
        return "index";
    }

    @PostMapping("/upload")
    public String save(@ModelAttribute("fileUploadForm") FileUploadForm fileUploadForm) {
        MultipartFile file = fileUploadForm.getFile();

        try {
            // Process the uploaded file here
            // For example: convertExcelToList(file.getInputStream());
//            int totalCellNumbers=getTotalCellNumber(file.getInputStream());
            int totalCellNumbers=6;
            printData(file.getInputStream(),totalCellNumbers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "redirect:/TDS"; // Redirect to the upload page after processing
    }


//    @PostMapping("/upload")
//    public ResponseEntity<?> save(@RequestParam("file") MultipartFile file){
//
//        try {
////            convertExcelToList(file.getInputStream());
//            printData(file.getInputStream());
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        return ResponseEntity.ok(Map.of("message","file uploaded sucessfully"));
//    }

}
