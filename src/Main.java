import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

class GameGraphics extends JPanel {
	private Game game;
	
	public GameGraphics(Game game) {
		this.game = game;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		int[] dimensions = {(int) this.getSize().getWidth(), (int) this.getSize().getHeight() };
		
		double pole_width = dimensions[0] / 32, pole_height = dimensions[1] / 2;
		double pole_base_width = dimensions[0] / 6, pole_base_height = dimensions[1] /32;
		double disc_height = Math.min(pole_height / game.get_numdiscs(), pole_base_height);
		
		/* Different colors for different discs */
		String[] colorsToRotate = { "#FF0000", "#FF7700", "#FFDD00", "#00FF00", "#0000FF", "#8A2BE2", "#C77DF3" };
		
		for (int i = 0; i < 3; i++) {
			/* Draw poles */
			g2d.setColor(Color.decode("#f4a460"));
			g2d.fill(new Rectangle2D.Double((2*i+1)*dimensions[0] / 6 - pole_width/2, dimensions[1] / 3, pole_width, pole_height));
			g2d.fill(new Rectangle2D.Double((2*i+1)*dimensions[0] / 6 - pole_base_width/2, dimensions[1] / 3 + pole_height, pole_base_width, pole_base_height));
			
			/* Draw discs */
			for (int disc = 0; disc < game.get_tower(i).size(); disc++) {
				int discsize = game.get_tower(i).get_disc(disc);
				g2d.setColor(Color.decode(colorsToRotate[discsize % colorsToRotate.length]));
				
				/* Scale disc width on number of disks */
				double disc_width = ((double)(discsize + 2) / game.get_numdiscs()) * pole_base_width;
				g2d.fill(new Rectangle2D.Double((2*i+1)*dimensions[0] / 6 - disc_width/2, dimensions[1] / 3 + (pole_height - disc_height*disc) - pole_base_height, disc_width, disc_height));
			}
		}
	}
}
class TopPanel extends JPanel {
	private Game game;
	private JTextField disc_setter;
	private JLabel num_moves_label;
	
	public TopPanel(Game game, Main parent) {
		this.game = game;
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		/* Setting the number of discs in this game */
		JLabel numdiscs_label = new JLabel("Discs: ");
		this.add(numdiscs_label);
		this.disc_setter = new JTextField("" + game.get_numdiscs());
		this.disc_setter.setColumns(2);
		this.disc_setter.setActionCommand("New game");
		this.disc_setter.addActionListener(parent);
		this.add(this.disc_setter);
		
		/* Displays number of moves currently done / number of moves left for optimal solution */ 
		this.num_moves_label = new JLabel();
		this.add(num_moves_label);
		
		/* Click to go to next step in the solution */
		JButton next_move_button = new JButton(">");
		next_move_button.setActionCommand("Next Move");
		next_move_button.addActionListener(parent);
		this.add(next_move_button);
		
		this.refresh();
	}
	
	/* Resets count of moves */
	public void refresh() {
		this.num_moves_label.setText("Moves: " + game.get_moves() + "/" + game.min_solution_moves());
	}
	
	/* Passes on the value given to the disc_setter textfield to actually set the game's number of discs */
	public int get_new_discs() {
		return Integer.parseInt(this.disc_setter.getText());
	}
	
}
public class Main extends JFrame implements ActionListener {
	private TopPanel toppanel;
	private Game game;
	private GameGraphics graphics;
	
	public Main(int numdiscs) {
		this.setLayout(new BorderLayout());
		
		this.game = new Game(numdiscs);
		this.graphics = new GameGraphics(game);
		this.toppanel = new TopPanel(game, this);
		
		this.add(toppanel, BorderLayout.PAGE_START);
		this.add(graphics, BorderLayout.CENTER);
		
		this.setTitle("Tower of Hanoi Solver");
		this.setSize(800, 600);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String actioncommand = e.getActionCommand();
		/* Resets game with new number of discs */
		if (actioncommand.equals("New game")) {
			game.reset(toppanel.get_new_discs());
			toppanel.refresh();
		}
		
		/* Shows next step in solution*/
		if (actioncommand.equals("Next Move")) {
			game.step();
			toppanel.refresh();
		}
		
		this.graphics.repaint();
	}
	
	public static void main(String args[]) {
		Main main = new Main(6);
	}
}
