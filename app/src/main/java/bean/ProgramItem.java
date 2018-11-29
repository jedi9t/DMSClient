package bean;

import java.util.List;

public class ProgramItem {
	
	public long programId;
	List<LoadItem> loadItemList;
	public long getProgramId() {
		return programId;
	}
	public void setProgramId(long programId) {
		this.programId = programId;
	}
	public List<LoadItem> getLoadItemList() {
		return loadItemList;
	}
	public void setLoadItemList(List<LoadItem> loadItemList) {
		this.loadItemList = loadItemList;
	}
	public ProgramItem(long programId, List<LoadItem> loadItemList) {
		super();
		this.programId = programId;
		this.loadItemList = loadItemList;
	}
	
	
}
