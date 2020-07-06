package com.payment.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.Builder;

@Builder
@Entity
public class CardHistory {
	
	//좌로 정렬 20글자
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String cardHistNumber;
	@NotNull
	@Column(columnDefinition="text")
	private String cardInterfaceString;

}
