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
    @NamedQuery(name = "MaintenanceSchedule.findByDate", query = "SELECT m FROM MaintenanceSchedule m WHERE m.date = :date"),
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
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Basic(optional = false)
    @NotNull
    @Column(name = "next_date")
    @Temporal(TemporalType.DATE)
    private Date nextDate;
    @JoinColumn(name = "equipment_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Equipment equipmentId;
    @JoinColumn(name = "frequency_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private MaintenanceFrequency frequencyId;
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private MaintenanceType typeId;

    public MaintenanceSchedule() {
    }

    public MaintenanceSchedule(Long id) {
        this.id = id;
    }

    public MaintenanceSchedule(Long id, Date date, Date nextDate) {
        this.id = id;
        this.date = date;
        this.nextDate = nextDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public MaintenanceFrequency getFrequencyId() {
        return frequencyId;
    }

    public void setFrequencyId(MaintenanceFrequency frequencyId) {
        this.frequencyId = frequencyId;
    }

    public MaintenanceType getTypeId() {
        return typeId;
    }

    public void setTypeId(MaintenanceType typeId) {
        this.typeId = typeId;
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
