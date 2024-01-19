package com.examnotes.service;

import com.examnotes.entity.Notes;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Stream;

public interface NotesService {

    Stream<Notes> fetchNotes();

    Notes storeNotes(MultipartFile file);

    Notes getFile(String id);

}
