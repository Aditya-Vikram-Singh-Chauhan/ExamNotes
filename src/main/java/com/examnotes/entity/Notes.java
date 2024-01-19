package com.examnotes.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "notes")
public class Notes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long notesID;
    private String notesName;
    private String type;
    private byte[] data;

    public Notes(String fileName, String contentType, byte[] bytes) {
        this.notesName = fileName;
        this.type = contentType;
        this.data = bytes;
    }
}
