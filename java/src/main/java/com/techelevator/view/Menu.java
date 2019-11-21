package com.techelevator.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Menu {

	private PrintWriter out;
	private Scanner in;

	public Menu(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while(choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if(selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch(NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if(choice == null) {
			out.println("\n*** "+userInput+" is not a valid option ***\n");
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println();
//		ArrayList<Object> optionsArrayList = new ArrayList<Object>(Arrays.asList(options));
//		int numbOfArrayLists = options.length/5;
//		if (options.length % 5 != 0) {
//			numbOfArrayLists++;
//		}
//		
//		ArrayList<ArrayList> optionArrays = new ArrayList<ArrayList>();
//		for (int i = 0; i < numbOfArrayLists; i++) {
//			ArrayList<Object> thisList = new ArrayList<Object>();
//			int jMax = (optionsArrayList.size() >= 5) ? 5 : optionsArrayList.size();
//			for (int j = 0; j < jMax; j++) {
//				thisList.add(optionsArrayList.remove(j));
//			}
//			optionArrays.add(thisList);
//		}
//		
//		for (int n = 0; n < 5; n++) {
//			for (ArrayList list : optionArrays) {
//				for (int m = 0; m < list.size(); m++) {
//					
//				}
//				
//			}
//		}
		for(int i = 0; i < options.length; i++) {
			int optionNum = i+1;
			out.println(optionNum+") "+options[i]);
		}
		out.print("\nPlease choose an option >>> ");
		out.flush();
	}
}
