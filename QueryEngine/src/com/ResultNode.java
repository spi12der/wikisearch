package com;

public class ResultNode implements Comparable<ResultNode>
{
	private int docId;
	private long tfScore;
	
	public int getDocId() {
		return docId;
	}
	public void setDocId(int docId) {
		this.docId = docId;
	}
	
	public long getTfScore() {
		return tfScore;
	}
	public void setTfScore(long tfScore) {
		this.tfScore = tfScore;
	}
	
	@Override
	public int compareTo(ResultNode o) 
	{
		return (int)(o.getTfScore()-this.tfScore);
	}
	
	
}
