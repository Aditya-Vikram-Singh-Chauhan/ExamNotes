package com.examnotes.service;

import com.examnotes.entity.Notes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.examnotes.repository.NotesRepo;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class NotesServiceImpl implements NotesService{

    @Autowired
    private NotesRepo nr;

    @Override
    public Stream<Notes> fetchNotes() {
        return nr.findAll().stream();
    }

    @Override
    public Notes storeNotes(MultipartFile file) {
        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Notes FileDB = new Notes(fileName, file.getContentType(), file.getBytes());
            System.out.println(FileDB);
            return nr.save(FileDB);
        } catch (Exception e){
            throw new RuntimeException("Cannot Store the note because of :"+e.getMessage());
        }


    }

    public Notes getFile(String id) {
        Optional<Notes> note = nr.findById(Long.parseLong(id));
        //return note.map(Notes::getData).orElse(null);
        return nr.findById(Long.parseLong(id)).get();
    }
}
