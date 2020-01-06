package com.internship.evaluation.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "stream_time_table")
public class StreamTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Stream is required")
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stream_id", referencedColumnName = "id")
    private Stream stream;

    @NotNull(message = "Time limit to take the test is required")
    @Column(name = "time_min")
    private Integer timeTest;

    @Override
    public String toString() {
        return "StreamTime{" +
                "id=" + id +
                ", timeTest=" + timeTest +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StreamTime that = (StreamTime) o;
        return id.equals(that.id) &&
                timeTest.equals(that.timeTest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, timeTest);
    }
}
