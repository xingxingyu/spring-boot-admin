package com.test.mysql.model;




public class DeviceQo extends PageQo {
	
	 private long id;
	    
	    private String num;
	    
	    private String departments;

	    private String ip;
	    
	    private String port;
	    
	    private String examine;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getDepartments() {
			return departments;
		}

		public void setDepartments(String departments) {
			this.departments = departments;
		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		public String getPort() {
			return port;
		}

		public void setPort(String port) {
			this.port = port;
		}

		public String getExamine() {
			return examine;
		}

		public void setExamine(String examine) {
			this.examine = examine;
		}

		public String getNum() {
			return num;
		}

		public void setNum(String num) {
			this.num = num;
		}
	    
	    
	
}
