/**
 * @author Axel Samuelsson
 */

import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;

public class Sokoban {
	ArrayList<ArrayList<Integer>> squares;
	ArrayList<ArrayList<String>> path;
	int player_x, player_y;

	static final int BLOCK = 0,
					  FREE = 1,
					  GOAL = 2,
					PLAYER = 3;

	public Sokoban () {
		squares = new ArrayList<>();
		path = new ArrayList<>();

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String s;
		int i = 0;
		try {
			while ((s = in.readLine()) != null && s.length() != 0) {
				squares.add(new ArrayList<Integer>());
				path.add(new ArrayList<String>());
				for(int j=0; j < s.length(); j++) {
					switch(s.charAt(j)) {
						case '#':
						case '$':
						case '*':
							squares.get(i).add(BLOCK);
							path.get(i).add("");
							break;
						case ' ':
							squares.get(i).add(FREE);
							path.get(i).add("");
							break;
						case '.':
							squares.get(i).add(GOAL);
							path.get(i).add("");
							break;
						case '@':
							squares.get(i).add(PLAYER);
							path.get(i).add("");
							player_x = j;
							player_y = i;
							break;
						case '+':
							System.out.println("");
							return;
					}
				}
				i++;
			}
		} catch (IOException e) {}

		System.out.println(breadthFirstSearch());
	}

	private String breadthFirstSearch() {
		LinkedList<Integer> q = new LinkedList<Integer>();
		LinkedList<Integer> qx = new LinkedList<Integer>();
		LinkedList<Integer> qy = new LinkedList<Integer>();

		q.add(squares.get(player_y).get(player_x));
		qx.add(player_x);
		qy.add(player_y);

		int cur, cur_x, cur_y;
		while(q.peek() != null) {
			cur = q.remove();
			cur_x = qx.remove();
			cur_y = qy.remove();

			if(cur == GOAL) return path.get(cur_y).get(cur_x);
			if(cur == BLOCK) continue;

			// UP
			if(path.get(cur_y-1).get(cur_x) == "") {
				q.add(squares.get(cur_y-1).get(cur_x));
				qx.add(cur_x);
				qy.add(cur_y-1);
				path.get(cur_y-1).set(cur_x, path.get(cur_y).get(cur_x)+"U");
			}
			// RIGHT
			if(path.get(cur_y).get(cur_x+1) == "") {
				q.add(squares.get(cur_y).get(cur_x+1));
				qx.add(cur_x+1);
				qy.add(cur_y);
				path.get(cur_y).set(cur_x+1, path.get(cur_y).get(cur_x)+"R");
			}
			// DOWN
			if(path.get(cur_y+1).get(cur_x) == "") {
				q.add(squares.get(cur_y+1).get(cur_x));
				qx.add(cur_x);
				qy.add(cur_y+1);
				path.get(cur_y+1).set(cur_x,path.get(cur_y).get(cur_x)+"D");
			}
			// LEFT
			if(path.get(cur_y).get(cur_x-1) == "") {
				q.add(squares.get(cur_y).get(cur_x-1));
				qx.add(cur_x-1);
				qy.add(cur_y);
				path.get(cur_y).set(cur_x-1,path.get(cur_y).get(cur_x)+"L");
			}
		}
		return "no path";
	}

	public static void main (String[] args) {
		new Sokoban();
	}
}
