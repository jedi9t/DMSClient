package bean;

import java.util.ArrayList;
import java.util.List;

public class ProgramPublish {

	int total;
	String messageId;
	List<Schedule> rows;
	List<UITemplate> uITemplateList;
	
	


	public List<UITemplate> getuITemplateList() {
		return uITemplateList;
	}

	public void setuITemplateList(List<UITemplate> uITemplateList) {
		this.uITemplateList = uITemplateList;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public List<Schedule> getRows() {
		return rows;
	}

	public void setRows(List<Schedule> rows) {
		this.rows = rows;
	}

	/*public List<UITemplate> getTemplateList() {
		List<UITemplate> uITemplates = new ArrayList<UITemplate>();
		if (rows != null) {

			List<Scheduleoftime> scheduleoftimes = rows.get(0).getScheduleoftimes();

			for (Scheduleoftime scheduleoftime : scheduleoftimes) {
				List<UITemplate> templates = scheduleoftime.getTemplates();
				for (UITemplate uITemplate : templates) {
					if (!getIsContain(uITemplates, uITemplate)) {
						uITemplates.add(uITemplate);
					}
				}
			}

		}
		if (uITemplates.size() == 0) {
			return null;
		}

		return uITemplates;

	}

	public boolean getIsContain(List<UITemplate> uITemplates, UITemplate uITemplate) {
		for (UITemplate template : uITemplates) {
			if (template.getId() == uITemplate.getId()) {
				return true;
			}
		}
		return false;
	}*/

}
