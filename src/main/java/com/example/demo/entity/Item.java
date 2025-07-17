package com.example.demo.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column
    private String picturelink;

    @Column(length = 1000)
    private String detail;
    
    @Column(nullable = false)
    private String genre;

    @OneToOne(mappedBy = "item", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Stock stock;
}
