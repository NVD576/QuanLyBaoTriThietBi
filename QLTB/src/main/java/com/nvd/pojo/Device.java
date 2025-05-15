/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.nvd.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ADMIN
 */
@Entity
@Table(name = "device")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Device.findAll", query = "SELECT d FROM Device d"),
    @NamedQuery(name = "Device.findById", query = "SELECT d FROM Device d WHERE d.id = :id"),
    @NamedQuery(name = "Device.findByName", query = "SELECT d FROM Device d WHERE d.name = :name"),
    @NamedQuery(name = "Device.findByDate", query = "SELECT d FROM Device d WHERE d.date = :date"),
    @NamedQuery(name = "Device.findByManufacturer", query = "SELECT d FROM Device d WHERE d.manufacturer = :manufacturer"),
    @NamedQuery(name = "Device.findByImage", query = "SELECT d FROM Device d WHERE d.image = :image")})
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "name")
    private String name;
    @Column(name = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date date;
    @Size(max = 100)
    @Column(name = "manufacturer")
    private String manufacturer;
    @Size(max = 255)
    @Column(name = "image")
    private String image;
    @OneToMany(mappedBy = "deviceId", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Repair> repairSet;

    @OneToMany(mappedBy = "deviceId", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Issue> issueSet;

    @ManyToOne
    @JoinColumn(name = "base_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"deviceSet"}) // hoặc thuộc tính bạn muốn bỏ qua của Base
    private Base baseId;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"deviceSet"})
    private Category categoryId;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    @JsonIgnoreProperties({"deviceSet"}) // hoặc các thuộc tính bạn muốn bỏ qua của Status
    private Status statusId;

    @OneToMany(mappedBy = "deviceId", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Maintenance> maintenanceSet;

    public Device() {
    }

    public Device(Integer id) {
        this.id = id;
    }

    public Device(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @XmlTransient
    public Set<Repair> getRepairSet() {
        return repairSet;
    }

    public void setRepairSet(Set<Repair> repairSet) {
        this.repairSet = repairSet;
    }

    @XmlTransient
    public Set<Issue> getIssueSet() {
        return issueSet;
    }

    public void setIssueSet(Set<Issue> issueSet) {
        this.issueSet = issueSet;
    }

    public Base getBaseId() {
        return baseId;
    }

    public void setBaseId(Base baseId) {
        this.baseId = baseId;
    }

    public Category getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Category categoryId) {
        this.categoryId = categoryId;
    }

    public Status getStatusId() {
        return statusId;
    }

    public void setStatusId(Status statusId) {
        this.statusId = statusId;
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
        if (!(object instanceof Device)) {
            return false;
        }
        Device other = (Device) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.nvd.pojo.Device[ id=" + id + " ]";
    }

    @Transient
    private MultipartFile file;

    /**
     * @return the file
     */
    public MultipartFile getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
