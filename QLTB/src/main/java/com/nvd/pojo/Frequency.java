/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author ADMIN
 */
@Entity
@Table(name = "frequency")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Frequency.findAll", query = "SELECT f FROM Frequency f"),
    @NamedQuery(name = "Frequency.findById", query = "SELECT f FROM Frequency f WHERE f.id = :id"),
    @NamedQuery(name = "Frequency.findByFrequency", query = "SELECT f FROM Frequency f WHERE f.frequency = :frequency")})
public class Frequency implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "frequency")
    private String frequency;
    @OneToMany(mappedBy = "frequencyId")
    @JsonIgnore
    private Set<Maintenance> maintenanceSet;

    public Frequency() {
    }

    public Frequency(Integer id) {
        this.id = id;
    }

    public Frequency(Integer id, String frequency) {
        this.id = id;
        this.frequency = frequency;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    @XmlTransient
    public Set<Maintenance> getMaintenanceSet() {
        return maintenanceSet;
    }

    public void setMaintenanceSet(Set<Maintenance> maintenanceSet) {
        this.maintenanceSet = maintenanceSet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Frequency)) {
            return false;
        }
        Frequency other = (Frequency) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.nvd.pojo.Frequency[ id=" + id + " ]";
    }
    
}
