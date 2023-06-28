package it.polito.tdp.yelp.model;

public class Adiacenza {
	
	public Adiacenza(User u1, User u2, int peso) {
		super();
		this.u1 = u1;
		this.u2 = u2;
		this.peso = peso;
	}
	private User u1;
	private User u2;
	private int peso;
	public User getU1() {
		return u1;
	}
	public void setU1(User u1) {
		this.u1 = u1;
	}
	public User getU2() {
		return u2;
	}
	public void setU2(User u2) {
		this.u2 = u2;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + peso;
		result = prime * result + ((u1 == null) ? 0 : u1.hashCode());
		result = prime * result + ((u2 == null) ? 0 : u2.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Adiacenza other = (Adiacenza) obj;
		if (peso != other.peso)
			return false;
		if (u1 == null) {
			if (other.u1 != null)
				return false;
		} else if (!u1.equals(other.u1))
			return false;
		if (u2 == null) {
			if (other.u2 != null)
				return false;
		} else if (!u2.equals(other.u2))
			return false;
		return true;
	}

}
