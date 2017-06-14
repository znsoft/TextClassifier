package com.irvil.nntextclassifier.dao;

import com.irvil.nntextclassifier.model.VocabularyWord;

import java.util.List;

public interface VocabularyWordDAO {
  List<VocabularyWord> getAll();

  void addAll(List<VocabularyWord> vocabulary) throws EmptyRecordException, AlreadyExistsException;
}
