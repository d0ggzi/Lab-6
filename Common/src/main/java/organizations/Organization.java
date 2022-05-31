package organizations;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * Класс Организации с необходимыми по заданию свойствами
 *
 * @author Max Laptev
 * @version 1.0
 */

public class Organization implements Comparable<Organization>, Serializable {

    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private final ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Integer annualTurnover; //Поле не может быть null, Значение поля должно быть больше 0
    private Long employeesCount; //Поле не может быть null, Значение поля должно быть больше 0
    private OrganizationType type; //Поле может быть null
    private Address postalAddress; //Поле может быть null

    public Organization(long id, String name, Coordinates coordinates, Integer annualTurnover, Long employeesCount, OrganizationType type, Address postalAddress){
        super();
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = ZonedDateTime.now();
        this.annualTurnover = annualTurnover;
        this.employeesCount = employeesCount;
        this.type = type;
        this.postalAddress = postalAddress;
    }

    public Organization(long id, String name, Coordinates coordinates, ZonedDateTime creationDate, Integer annualTurnover, Long employeesCount, OrganizationType type, Address postalAddress){
        super();
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.annualTurnover = annualTurnover;
        this.employeesCount = employeesCount;
        this.type = type;
        this.postalAddress = postalAddress;
    }


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAnnualTurnover() {
        return annualTurnover;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Address getPostalAddress() {
        return postalAddress;
    }

    public Long getEmployeesCount() {
        return employeesCount;
    }

    public OrganizationType getType() {
        return type;
    }

    public String toXmlFormat(){
        String xmlString = "";

        xmlString += String.format("<id>%d</id>\n", this.id);
        xmlString += String.format("<name>%s</name>\n", this.name);
        xmlString += String.format("<coordinates-x>%s</coordinates-x>\n", this.coordinates.getX());
        xmlString += String.format("<coordinates-y>%s</coordinates-y>\n", this.coordinates.getY());
        xmlString += String.format("<creationDate>%s</creationDate>\n", this.creationDate);
        xmlString += String.format("<annualTurnover>%s</annualTurnover>\n", this.annualTurnover);
        xmlString += String.format("<employeesCount>%s</employeesCount>\n", this.employeesCount);
        xmlString += String.format("<organizationType>%s</organizationType>\n", this.type);
        xmlString += String.format("<zipCode>%s</zipCode>\n", this.postalAddress.getZipCode());
        xmlString += String.format("<location-x>%s</location-x>\n", this.postalAddress.getTown().getX());
        xmlString += String.format("<location-y>%s</location-y>\n", this.postalAddress.getTown().getY());
        xmlString += String.format("<location-z>%s</location-z>\n", this.postalAddress.getTown().getZ());


        return xmlString;
    }

    @Override
    public String toString() {
        return "Организация: id=" + id + ", название=" + name + ", координаты=" + coordinates + ", дата создания=" + creationDate.toString().split("T")[0] + ", ежегодный оборот=" + annualTurnover + ", количество работников=" + employeesCount + ", тип=" + type + ", адрес=[" + postalAddress +"]";
    }

    @Override
    public int compareTo(Organization p) {
        return (int) this.annualTurnover - p.getAnnualTurnover();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setAnnualTurnover(Integer annualTurnover) {
        this.annualTurnover = annualTurnover;
    }

    public void setEmployeesCount(Long employeesCount) {
        this.employeesCount = employeesCount;
    }

    public void setType(OrganizationType type) {
        this.type = type;
    }

    public void setPostalAddress(Address postalAddress) {
        this.postalAddress = postalAddress;
    }
}