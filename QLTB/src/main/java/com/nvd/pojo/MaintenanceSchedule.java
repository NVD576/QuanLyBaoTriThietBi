/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.pojo;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ADMIN
 */
@Entity
@Table(name = "maintenance_schedule")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "MaintenanceSchedule.findAll", query = "SELECT m FROM MaintenanceSchedule m"),
    @NamedQuery(name = "MaintenanceSchedule.findById", query = "SELECT m FROM MaintenanceSchedule m WHERE m.id = :id"),
    @NamedQuery(name = "MaintenanceSchedule.findByFrequency", query = "SELECT m FROM MaintenanceSchedule m WHERE m.frequency = :frequency"),
    @NamedQuery(name = "MaintenanceSchedule.findByType", query = "SELECT m FROM MaintenanceSchedule m WHERE m.type = :type"),
    @NamedQuery(name = "MaintenanceSchedule.findByNextDate", query = "SELECT m FROM MaintenanceSchedule m WHERE m.nextDate = :nextDate")})
public class MaintenanceSchedule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "frequency")
    private String frequency;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @NotNull
    @Column(name = "next_date")
    @Temporal(TemporalType.DATE)
    private Date nextDate;
    @JoinColumn(name = "equipment_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Equipment equipmentId;

    public MaintenanceSchedule() {
    }

    public MaintenanceSchedule(Long id) {
        this.id = id;
    }

    public MaintenanceSchedule(Long id, String frequency, String type, Date nextDate) {
        this.id = id;
        this.frequency = frequency;
        this.type = type;
        this.nextDate = nextDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getNextDate() {
        return nextDate;
    }

    public void setNextDate(Date nextDate) {
        this.nextDate = nextDate;
    }

    public Equipment getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Equipment equipmentId) {
        this.equipmentId = equipmentId;
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
        if (!(object instanceof MaintenanceSchedule)) {
            return false;
        }
        MaintenanceSchedule other = (MaintenanceSchedule) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.nvd.pojo.MaintenanceSchedule[ id=" + id + " ]";
    }
    
}
