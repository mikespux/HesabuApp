package com.hesabu;

public class Account {
  private String id,pid,sprice,bprice,pname;

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public String getPid() {
	return pid;
}

public void setPid(String pid) {
	this.pid = pid;
}

public String getSprice() {
	return sprice;
}

public void setSprice(String sprice) {
	this.sprice = sprice;
}

public String getBPrice() {
	return bprice;
}

public void setBPrice(String bprice) {
	this.bprice = bprice;
}

public String getPname() {
	return pname;
}

public void setPname(String pname) {
	this.pname = pname;
}

@Override
public String toString() {
	return  pname;
}
  
}
