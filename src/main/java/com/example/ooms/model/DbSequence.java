package com.example.ooms.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection= "db_sequence")
public class DbSequence {
	
	@Id
	private String id;
	private int seq;

}
