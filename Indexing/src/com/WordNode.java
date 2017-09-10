package com;

public class WordNode implements Comparable<WordNode>
{
	private String word;
	private String postingString;
	private int index;
	
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getPostingString() {
		return postingString;
	}

	public void setPostingString(String postingString) {
		this.postingString = postingString;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public int compareTo(WordNode o) {
		return this.getWord().compareTo(o.getWord());
	}
}
