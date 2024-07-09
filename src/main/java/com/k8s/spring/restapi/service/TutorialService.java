package com.k8s.spring.restapi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.k8s.spring.restapi.model.Tutorial;

@Service
public class TutorialService {

    static List<Tutorial> tutorials = new ArrayList<Tutorial>();
    static long id = 0;

    static {
        IntStream.range(0, 10000).forEach(i -> {
            Tutorial t = new Tutorial();
            t.setId((int) Math.random() * 10000);
            t.setDescription(UUID.randomUUID().toString());
            tutorials.add(t);
        });
    }

    public List<Tutorial> findAll() {
        return tutorials;
    }

    public List<Tutorial> findByTitleContaining(String title) {
        return tutorials.stream().filter(tutorial -> tutorial.getTitle().contains(title)).toList();
    }

    public Tutorial findById(long id) {
        return tutorials.stream().filter(tutorial -> id == tutorial.getId()).findAny().orElse(null);
    }

    public Tutorial save(Tutorial tutorial) {
        // update Tutorial
        if (tutorial.getId() != 0) {
            long _id = tutorial.getId();

            for (int idx = 0; idx < tutorials.size(); idx++) {
                if (_id == tutorials.get(idx).getId()) {
                    tutorials.set(idx, tutorial);
                    break;
                }
            }

            return tutorial;
        }

        // create new Tutorial
        tutorial.setId(++id);
        tutorials.add(tutorial);
        return tutorial;
    }

    public void deleteById(long id) {
        tutorials.removeIf(tutorial -> id == tutorial.getId());
    }

    public void deleteAll() {
        tutorials.removeAll(tutorials);
    }

    public List<Tutorial> findByPublished(boolean isPublished) {
        return tutorials.stream().filter(tutorial -> isPublished == tutorial.isPublished()).toList();
    }
}
