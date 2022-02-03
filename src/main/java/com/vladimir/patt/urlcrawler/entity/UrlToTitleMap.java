package com.vladimir.patt.urlcrawler.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@ToString
@Entity
@Table(name = "URLTOTITLEMAP")
public class UrlToTitleMap {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private @Setter @Getter Long id;
	
	@Column(name = "url", nullable = false, unique = true)
	private @Setter @Getter String url;
	
	@Column(name = "title", nullable = false)
	private @Setter @Getter String title;

	public UrlToTitleMap(String url, String title) {
		this.url = url;
		this.title = title;
	}	
	   
}


