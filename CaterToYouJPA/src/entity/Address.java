package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Address {
	
	
	//field
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@OneToMany(mappedBy="delivery_address")
	private int id;
	
	@Column(name="street")
	private String street;
	
	@Column(name="street2")
	private String street2;
	
	@Column(name="city")
	private String city;
	
	@Column(name="state")
	private String state;
	
	@Column(name="zip")
	private int zip;
	
	
	
	
	
	
	
	
	
	//sets and gets
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getStreet2() {
		return street2;
	}
	public void setStreet2(String street2) {
		this.street2 = street2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getZip() {
		return zip;
	}
	public void setZip(int zip) {
		this.zip = zip;
	}
	
	
	//toString
	@Override
	public String toString() {
		return "Address [id=" + id + ", street=" + street + ", street2=" + street2 + ", city=" + city + ", state="
				+ state + ", zip=" + zip + "]";
	}
	
	
	
	
	

}