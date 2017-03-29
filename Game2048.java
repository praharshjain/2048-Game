import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Game2048 extends JFrame implements KeyListener {
	JLabel[][] label = new JLabel[4][4];
	int[][] value = new int[4][4];
	Random r = new Random();

	public int return2or4(boolean b) {
		if (b)
			return 2;
		else
			return 4;
	}

	public Game2048() {
		setTitle("Play 2048 Game");
		setSize(400, 400);
		setLayout(new GridLayout(4, 4));
		setVisible(true);
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				label[i][j] = new JLabel("", JLabel.CENTER);
				label[i][j].setFont(new Font("Avenir", Font.BOLD, 24));
				value[i][j] = 0;
				add(label[i][j]);
				label[i][j].setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
			}
		}
		addKeyListener(this);
		value[Math.abs(r.nextInt() % 4)][Math.abs(r.nextInt() % 4)] = return2or4(r.nextBoolean());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		display();
	}

	public String str(int a) {
		if (a != 0)
			return Integer.toString(a);
		else
			return "";
	}

	public void display() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				label[i][j].setText(str(value[i][j]));
			}
		}
	}

	public void keyPressed(KeyEvent ke) {
		int key = ke.getKeyCode();
		switch (key) {
		case KeyEvent.VK_UP:
			checkSuccess();
			if (!isEnd())
				moveUp();
			else
				gameOver();
			break;
		case KeyEvent.VK_DOWN:
			checkSuccess();
			if (!isEnd())
				moveDown();
			else
				gameOver();
			break;
		case KeyEvent.VK_RIGHT:
			checkSuccess();
			if (!isEnd())
				moveRight();
			else
				gameOver();
			break;
		case KeyEvent.VK_LEFT:
			checkSuccess();
			if (!isEnd())
				moveLeft();
			else
				gameOver();
			break;
		}
	}

	public void gameOver() {
		setTitle(":(");
		String s = "    GAMEOVER    ";
		for (int i = 0; i < s.length(); i++)
			label[i / 4][i % 4].setText(s.charAt(i) + "");
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++)
				System.out.print(value[i][j] + " ");
			System.out.println();
		}
	}

	public void moveLeft() {
		if (endLeft())
			return;
		for (int i = 0; i < 4; i++) {
			int[] queue = new int[4];
			int front = 0;
			int end = -1;
			for (int j = 0; j < 4; j++) {
				if (value[i][j] != 0) {
					queue[++end] = value[i][j];
					value[i][j] = 0;
				}
			}
			for (int j = 0; j < 4; j++) {
				if (front <= end && value[i][j] == 0)
					value[i][j] = queue[front++];
			}
		}
		mergeLeft();
		generateNewElement();
		display();
	}

	public void moveUp() {
		if (endUp())
			return;
		transpose();
		for (int i = 0; i < 4; i++) {
			int[] queue = new int[4];
			int front = 0;
			int end = -1;
			for (int j = 0; j < 4; j++) {
				if (value[i][j] != 0) {
					queue[++end] = value[i][j];
					value[i][j] = 0;
				}
			}
			for (int j = 0; j < 4; j++) {
				if (front <= end && value[i][j] == 0)
					value[i][j] = queue[front++];
			}
		}
		mergeLeft();
		generateNewElement();
		transpose();
		display();
	}

	public void moveRight() {
		if (endRight())
			return;
		for (int i = 0; i < 4; i++) {
			int[] stack = new int[4];
			int top = -1;
			for (int j = 0; j < 4; j++) {
				if (value[i][j] != 0) {
					stack[++top] = value[i][j];
					value[i][j] = 0;
				}
			}
			for (int j = 3; j >= 0; j--) {
				if (top >= 0 && value[i][j] == 0)
					value[i][j] = stack[top--];
			}
		}
		mergeRight();
		generateNewElement();
		display();
	}

	public void moveDown() {
		if (endDown())
			return;
		transpose();
		for (int i = 0; i < 4; i++) {
			int[] stack = new int[4];
			int top = -1;
			for (int j = 0; j < 4; j++) {
				if (value[i][j] != 0) {
					stack[++top] = value[i][j];
					value[i][j] = 0;
				}
			}
			for (int j = 3; j >= 0; j--) {
				if (top >= 0 && value[i][j] == 0)
					value[i][j] = stack[top--];
			}
		}
		mergeRight();
		generateNewElement();
		transpose();
		display();
	}

	public void mergeLeft() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				if (value[i][j] == value[i][j + 1]) {
					value[i][j] += value[i][j + 1];
					int k = j + 1;
					while (k < 3) {
						value[i][k] = value[i][k + 1];
						k++;
					}
					value[i][3] = 0;
				}
			}
		}
	}

	public boolean endLeft() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (value[i][j] == 0)
					return false;
			}
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				if (value[i][j] == value[i][j + 1])
					return false;
			}
		}
		return true;
	}

	public boolean endRight() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (value[i][j] == 0)
					return false;
			}
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 3; j > 0; j--) {
				if (value[i][j] == value[i][j - 1])
					return false;
			}
		}
		return true;
	}

	public boolean endUp() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (value[i][j] == 0)
					return false;
			}
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 4; j++) {
				if (value[i][j] == value[i + 1][j])
					return false;
			}
		}
		return true;
	}

	public boolean endDown() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (value[i][j] == 0)
					return false;
			}
		}
		for (int i = 3; i > 0; i--) {
			for (int j = 0; j < 4; j++) {
				if (value[i][j] == value[i - 1][j])
					return false;
			}
		}
		return true;
	}

	public void mergeRight() {
		for (int i = 0; i < 4; i++) {
			for (int j = 3; j > 0; j--) {
				if (value[i][j] == value[i][j - 1]) {
					value[i][j] += value[i][j - 1];
					int k = j - 1;
					while (k > 0) {
						value[i][k] = value[i][k - 1];
						k--;
					}
					value[i][0] = 0;
				}
			}
		}
	}

	public void generateNewElement() {
		boolean iszeros = false;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (value[i][j] == 0)
					iszeros = true;
				break;
			}
		}
		if (!iszeros)
			return;
		int indexi, indexj;
		indexi = Math.abs(r.nextInt() % 4);
		indexj = Math.abs(r.nextInt() % 4);
		while (value[indexi][indexj] != 0) {
			indexi = Math.abs(r.nextInt() % 4);
			indexj = Math.abs(r.nextInt() % 4);
		}
		value[indexi][indexj] = return2or4(r.nextBoolean());
	}

	public void transpose() {
		int[][] temp = new int[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++)
				temp[i][j] = value[i][j];
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++)
				value[i][j] = temp[j][i];
		}
	}

	public boolean isEnd() {
		return (endRight() && endDown() && endUp() && endLeft());
	}

	public void checkSuccess() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (value[i][j] == 2048) {
					setTitle("You won the game!");
					break;
				}
			}
		}
	}

	public void keyReleased(KeyEvent ke) {
	}

	public void keyTyped(KeyEvent ke) {
	}

	public static void main(String args[]) {
		Game2048 g = new Game2048();
	}
}
