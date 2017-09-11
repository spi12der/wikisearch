package com;

public class ResultNode implements Comparable<ResultNode>
{
	private int docId;
	private double tfScore;
	
	public int getDocId() {
		return docId;
	}
	public void setDocId(int docId) {
		this.docId = docId;
	}
	public double getTfScore() {
		return tfScore;
	}
	public void setTfScore(double tfScore) {
		this.tfScore = tfScore;
	}
	
	@Override
	public int compareTo(ResultNode o) 
	{
		return (int)(o.getTfScore()-this.tfScore);
	}
	
	
}
