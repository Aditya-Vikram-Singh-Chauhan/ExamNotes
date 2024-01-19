package com.examnotes.controller;

import com.examnotes.entity.Notes;
import com.examnotes.message.ResponseFile;
import com.examnotes.message.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.examnotes.service.NotesService;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class NotesController {

    @Autowired
    private NotesService ns;

    @GetMapping("/files/{id}")
    public String getFile(@PathVariable String id, Model model) {
        Notes pdfData = ns.getFile(id);
        if (pdfData != null) {
            // You may want to convert pdfData to Base64 or use another format
            //model.addAttribute("pdfData", pdfData.getData());
            model.addAttribute("pdfData", Base64.getEncoder().encodeToString(pdfData.getData()));

            return "reader"; // View name for rendering PDF
        } else {
            return "errorPage"; // Handle not found case
        }
    }

    @PostMapping("/UploadNotes")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile[] file) {
        String message = "";
        //System.out.println(file.getOriginalFilename());
        try {
            List<String> fileNames = new ArrayList<>();
            Arrays.asList(file).stream().forEach(fil -> {
                ns.storeNotes(fil);
                fileNames.add(fil.getOriginalFilename());
            });

            message = "Uploaded the files successfully: " + fileNames;
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Fail to upload files!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }
    @GetMapping("/files")
    public String getListFiles(Model model) {
        List<ResponseFile> files = ns.fetchNotes().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(dbFile.getNotesID() + "")
                    .toUriString();

            return new ResponseFile(
                    dbFile.getNotesName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length);
        }).collect(Collectors.toList());

        List<String> NoteNames = new ArrayList<>();
        List<String> NotesURL = new ArrayList<>();
        files.stream().forEach(i -> NoteNames.add(i.getName()));
        files.stream().forEach(i -> NotesURL.add(i.getUrl()));
        model.addAttribute("NoteNames", NoteNames);
        model.addAttribute("NotesURL", NotesURL);
        return "index";
        //return ResponseEntity.status(HttpStatus.OK).body(files);
    }

}
