package com.uni.data.analyzer.data.model.analysis;

import com.uni.data.analyzer.data.model.BaseEntity;
import com.uni.data.analyzer.data.model.UploadedFile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static javax.persistence.InheritanceType.TABLE_PER_CLASS;

@Entity
@Inheritance(strategy = TABLE_PER_CLASS)
public abstract class Analysis extends BaseEntity {

    @ManyToMany
    @JoinTable(name = "analysis_files",
            joinColumns = @JoinColumn(name = "analysis_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "file_id", referencedColumnName = "id"))
    private final List<UploadedFile> files;
    private final String error;

    protected Analysis() {
        this.error = null;
        this.files = new ArrayList<>();
    }

    public Analysis(String error) {
        files = new ArrayList<>();
        this.error = error;
    }

    public Optional<String> getError() {
        return Optional.ofNullable(error);
    }

    public abstract Map<String, Object> getValues();

    public List<UploadedFile> getFiles() {
        return files;
    }
}
