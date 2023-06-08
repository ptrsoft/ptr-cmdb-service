package com.synectiks.asset.response;

import lombok.*;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vpc implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private List<Cluster> clusters;
	
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vpc other = (Vpc) obj;
		return Objects.equals(name, other.name);
	}

//	public List<Cluster> getClusters() {
//		return clusters;
//	}
//
//	public void setClusters(List<Cluster> clusters) {
//		this.clusters = clusters;
//	}
	
}
