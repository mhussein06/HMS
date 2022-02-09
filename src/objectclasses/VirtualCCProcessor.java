package objectclasses;

public class VirtualCCProcessor {
	private String cardNum;
	private String expiration;
	private int csc;

	public VirtualCCProcessor(String cardNum, String expiration, int csc) {
		super();
		this.cardNum = cardNum;
		this.expiration = expiration;
		this.csc = csc;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getExpiration() {
		return expiration;
	}

	public void setExpiration(String expiration) {
		this.expiration = expiration;
	}

	public int getCsc() {
		return csc;
	}

	public void setCsc(int csc) {
		this.csc = csc;
	}

	@Override
	public int hashCode() {
		int hash = 9;
		String dateString = expiration.toString();
		hash = 37 * hash + cardNum.hashCode();
		hash = 37 * hash + csc;
		hash = 37 * hash + dateString.hashCode();
		return hash;
	}
	
	public static int hashCode(String expiration, String cardNum, int csc) {
		int hash = 9;
		String dateString = expiration.toString();
		hash = 37 * hash + cardNum.hashCode();
		hash = 37 * hash + csc;
		hash = 37 * hash + dateString.hashCode();
		return hash;
	}

	public String payBill(int cctoken, float amt) {
		return "Approved";
	}
}
