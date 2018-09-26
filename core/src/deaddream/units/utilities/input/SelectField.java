package deaddream.units.utilities.input;

public class SelectField {
    private float startX;
    private float startY;
    private float endX;
    private float endY;
    private boolean isActive = false;
    private boolean isReady = false;
    private float range = 1f;
    public void update(float x, float y) {
    	if (!isActive) {
    		startX = x;
    		startY = y;
    		isActive = true;
    	}
    	endX = x;
    	endY = y;
    	if (Math.abs(startX-endX) > range && Math.abs(startY - endY) > range) {
    		isReady = true;
    	}else {
    		isReady = false;
    	}
    }
    
    public void drop() {
    	startX = 0.0f;
    	startY = 0.0f;
    	endX = 0.0f;
    	endY = 0.0f;
    	isActive = false;
    	isReady = false;
    }
    
    public boolean getIsReady() {
    	return isReady;
    }
    
    public float getStartX() {
    	return startX;
    }
    
    public float getStartY() {
    	return startY;
    }
    
    public float getEndX() {
    	return endX;
    }
    
    public float getEndY() {
    	return endY;
    }
    
    public boolean isInSelectField(float x, float y) {
    	return isInSelectField(x, y, startX, endX, startY, endY);
    }
    
    public static boolean isInSelectField(float x, float y, float x1, float x2, float y1, float y2) {
    	float smallerX = x1 < x2 ? x1 : x2;
		float smallerY = y1 < y2 ? y1 : y2;			
		float biggerX = x1 > x2 ? x1 : x2;
		float biggerY = y1 > y2 ? y1 : y2;
		
    	if ((x >= smallerX && x <= biggerX) && (y >= smallerY && y <= biggerY)) {
    		return true;
    	}
    	return false;
    }
}
