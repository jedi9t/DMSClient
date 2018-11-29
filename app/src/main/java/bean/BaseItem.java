package bean;

import android.graphics.Bitmap;

public class BaseItem {
	int id;	
	protected Bitmap image;	
	protected Bitmap select_image;	
	
	private String text;
	private String action;	
	int tag;
	
	int image_n;		
	int image_s;

	public BaseItem(){
		
	}
	
    public BaseItem(String _text){
		text=_text;
	}
    
    public BaseItem(int _id,String _text){
    	id=_id;
		text=_text;
	}
    
    public BaseItem(int _id,int image_n,int image_s,String _text){
    	id=_id;
    	this.image_n=image_n;
    	this.image_s=image_s;
		text=_text;
	}
    
    
    public BaseItem(int _id,int _tag){
    	id=_id;
		tag=_tag;
	}
    
    public BaseItem(int _id,Bitmap _image){
    	id=_id;
    	image=_image;
	}

	
     public BaseItem(int image_n,String _text,String _action){
    	this.image_n=image_n;
		text=_text;
		action=_action;
	}
     
     public BaseItem(Bitmap _image,String _text,String _action){
 		image=_image;
 		text=_text;
 		action=_action;
 	}
     
     public BaseItem(Bitmap _image,Bitmap selec_image,String _text,String _action){
 		image=_image;
 		select_image=selec_image;
 		text=_text;
 		action=_action;
 	}             
     
     public int getId() {
 		return id;
 	}

 	public void setId(int id) {
 		this.id = id;
 	}	
	
 	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}
	
     public Bitmap getSelect_image() {
 		return select_image;
 	}

 	public void setSelect_image(Bitmap select_image) {
 		this.select_image = select_image;
 	}
 	
	public Bitmap getImage() {
		return image;
	}
	public void setImage(Bitmap image) {
		this.image = image;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	public int getImage_n() {
		return image_n;
	}

	public void setImage_n(int image_n) {
		this.image_n = image_n;
	}

	public int getImage_s() {
		return image_s;
	}

	public void setImage_s(int image_s) {
		this.image_s = image_s;
	}	
}
