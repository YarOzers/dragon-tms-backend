package com.example.dragontmsbackend.repository;

import com.example.dragontmsbackend.model.folder.Folder;
import com.example.dragontmsbackend.model.testcase.TestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestCaseRepository extends JpaRepository<TestCase, Long> {

    List<TestCase> findTestCasesByFolder(Folder folder);
}
