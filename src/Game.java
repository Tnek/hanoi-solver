import java.util.ArrayList;
/* 
 * Data representation of a tower - It's basically a stack
 */
class Tower {
	private ArrayList<Integer> discs;
	public Tower() {
		this.discs = new ArrayList<Integer>();
	}
	
	public int get_top_disc() {
		return discs.get(discs.size() - 1);
	}
	
	public boolean add_disc(int disc) {
		if (this.size() == 0 || this.get_top_disc() > disc) {
			discs.add(disc);
			return true;
		}
		return false;
	}
	
	public int pop() {
		return discs.remove(discs.size() - 1);
	}
	
	public boolean move_disc(Tower destination) {
		if (this.size() != 0 && (destination.size() == 0 || this.get_top_disc() < destination.get_top_disc())) {
			return destination.add_disc(this.pop());
		}
		return false;
	}
	
	public int size() {
		return this.discs.size();
	}
	
	public ArrayList<Integer> get_discs() {
		return this.discs;
	}
	
	public int get_disc(int index) {
		return this.discs.get(index);
	}
}

/* 
 * Game Controller class
 */
public class Game {
	private int numdiscs;
	private Tower[] towers = new Tower[3];
	private int num_moves = 0;
	public Game(int numdiscs) {
		this.reset(numdiscs);
	}
	
	/* Creates new game with new number of discs */
	public void reset(int numdiscs) {
		this.numdiscs = numdiscs;
		for (int i = 0; i < 3; i++) {
			this.towers[i] = new Tower();
		}
		for (int i = 0; i < numdiscs; i++) {
			this.towers[0].add_disc(numdiscs - i);
		}
		this.num_moves = 0;
	}
	
	public int get_numdiscs() {
		return numdiscs;
	}
	
	public boolean move_disc(int source, int destination) {
		if (this.towers[source].move_disc(this.towers[destination])) {
			this.num_moves++;
			return true;
		}
		return false;
	}
	
	public Tower get_tower(int i) {
		return towers[i];
	}
	
	public int get_moves() {
		return this.num_moves;
	}
	
	/* Finds minimum number of moves required to solve this */
	public int min_solution_moves() {
		return (int) (Math.pow(2, numdiscs) - 1);
	}
	
	/* Takes one step in for the interative solution for the Tower of Hanoi */
	private int step = 0;
	public void step() {
		if (this.num_moves < this.min_solution_moves()) {	
			switch (step) { 
				case 0:
					if (numdiscs % 2 == 0) {
						if (!this.move_disc(0, 1)) {
							this.move_disc(1,  0);
						}
					} else {
						if (!this.move_disc(0, 2)) {
							this.move_disc(2,  0);
						}
					}
					break;
					
				case 1:
					if (numdiscs % 2 == 0) {
						if (!this.move_disc(0, 2)) {
							this.move_disc(2,  0);
						}
						
					} else {
						if (!this.move_disc(0, 1)) {
							this.move_disc(1,  0);
						}
					}
					break;
					
				case 2:
					if (numdiscs % 2 == 0) {
						if (!this.move_disc(1, 2)) {
							this.move_disc(2,  1);
						}
						
					} else {
						if (!this.move_disc(2, 1)) {
							this.move_disc(1,  2);
						}
					}
					break;
			}
			step = (step + 1) % 3;
		}
	}
}
