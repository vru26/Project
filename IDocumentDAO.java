package com.lnt.mvc.dao;

import java.util.List;

import com.lnt.mvc.model.Document;

public interface IDocumentDAO {
	public void remove(Integer id);

	public Document get(Integer id);

	public List<Document> list();

	public void save(Document document);

}
