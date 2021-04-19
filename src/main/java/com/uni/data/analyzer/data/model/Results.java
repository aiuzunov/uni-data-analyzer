package com.uni.data.analyzer.data.model;

import javax.persistence.*;

@Entity(name="Results")
public class Results {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

}
