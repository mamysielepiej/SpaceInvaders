package application;

import java.net.URISyntaxException;
import javax.swing.JFrame;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.WindowEvent;

/**
*	Kontroler sceny.
*	Przechowuje zmienne, kt�re s� pojemnikami na elementy sceny,
*	
*/
public class Controller implements Runnable{

    @FXML
    /**	Przycisk rozpoczynaj�cy now� gr�.	*/
    private Button nowaGra;

    @FXML
    /**	Etykieta, w kt�rej wyswietlony zostanie aktualny wynik.	*/
    private Label punkty;

    @FXML
    /**	Pojemnik w kt�rym znajd� si� wszystkie elementy poruszaj�ce si� po ekranie.	*/
    private Pane pane;
    
	    /**	Obiekt odtwarzaj�cy muzyk�.	*/
    	public MediaPlayer mp;
	    /**	Obiekt przechowuj�cy wygl�d statku.	*/
	    public ImageView statek;
	    /** Obiekt przechowuj�cy wygl�d pojedy�czego przeciwnika.	*/
	    public Image przeciwnikObraz;
	    /**	Tablica obraz�w, w kt�rej przechowani zostan� przeciwnicy.	*/
	    public ImageView[] przeciwnicy;
	    /**	Tablica warto�ci prawda/fa�sz, w kt�rej przechowana zostanie informacja,
	     * 	czy przeciwnik o danym indeksie zosta� trafiony.
	     */
	    public boolean[] czyPrzeciwnikTrafiony = new boolean[36];
	    /**	Wsp�rz�dne statku. */
	    public double x, y;
	    /**	Flaga przechowuj�ca informacje,
		 *	w kt�r� stron� powinni poruszy� si� przeciwnicy.
		 */
	    public boolean prawo, lewo, dol;
	    /**	Flaga kt�ra okre�la, czy gra w danej chwili trwa. */
	    public boolean endGame;
	
	/**
	 *	Funkcja ta uruchamia si� zaraz po konstruktorze klasy.
	 *	Ustawia zmienne na wartosci jakie powinny miec po starcie aplikacji i przed rozpoczeciem gry,
	 *	a takze zaczyna odtwarzac muzyke w programie.
	 */
	@FXML
    public void initialize(){
    	endGame = true;
    	wynik_int = 0;
    	try {
			mp = new MediaPlayer(new Media(getClass().getResource("/application/sound.mp3").toURI().toString()));	//podanie sciezki do pliu z muzyka
			mp.setCycleCount(100);
			mp.play();
    	} catch (URISyntaxException e) {
			e.printStackTrace();
		}
    }
	
	/**
	 *	Funkcja ta zostaje wywolana po wcisnieciu klawisza.
	 *	Ma za zadanie odswiezyc wynik wypisujac aktualna wartosc na ekran 
	 *	i w zaleznosci od tego, jaki klawisz zostal wcisniety zareagowac w odpowiedni sposob.
	 *	Dla klawiszy A i D zmienic x statku (poruszyc w lewo lub prawo)
	 *	Dla klawisza S wywolac funkcje jesli gra nie jest zacz�ta (flaga endGame nie jest ustawiona)
	 *	@param	e	zdarzenie wcisniecia klawisza, przechowuje kod wcisnietego klawisza
	 */
	public void handler(KeyEvent e){
		String wynik = "" + wynik_int;
		punkty.setText(wynik);
		if (e.getCode() == KeyCode.D) {
            if(x != 720) { 
            	x += 10;
    				pane.getChildren().get(36).setLayoutX(pane.getChildren().get(36).getLayoutX()+10);	
    		}
        } else if (e.getCode() == KeyCode.A) {
        	if(x != 10) {
        		x -= 10;
        		for(Node n : pane.getChildren()) {
    				if(n == pane.getChildren().get(36)) {
    					pane.getChildren().get(36).setLayoutX(pane.getChildren().get(36).getLayoutX()-10);
    				}
        		}
        	}
        } else if (e.getCode() == KeyCode.S) {
        	if(!endGame)
        		this.strzal();
        }
	}
	
	/**
	 *	Funkcja ta zostaje wywolana po zamknieciu g��wnego okna.
	 *	Konczy dzialanie programu.
	 *	@param	t	zdarzenie zamkniecia okna
	 */
	public void close(WindowEvent t) {
		Platform.exit();
        System.exit(0);
	}
	
	/**
	 * 	Funkcja ta wywo�uje po kliknieicu w przycisk rozpoczynaj�cy gr�.
	 * 	Konczy dzialanie w�tk�w poruszaj�cych przeciwnikami i pociskiem.
	 * 	Ustawia wszystkie zmienne na warto�ci pocz�tkowe.
	 * 	Inicializuje na nowo wszystkie elementy, kt�re maj� wy�wietla� sie i porusza� w oknie podczas gry.
	 * 	Tworzy w�tek poruszaj�cy przeciwnikami.
	 */
	@FXML
    public void StartGame() {
		endGame = true;
    	try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	wynik_int = 0;
    	punkty.setText("0");
    	endGame = false;
    	prawo = true;
    	lewo = false;
    	dol = false;
    	x = 360;
    	y = 520;
    	statek = new ImageView(new Image("/application/ship.png"));
    	for(int i = 0; i < 36; i++)
    		czyPrzeciwnikTrafiony[i] = false;
    	
    	while(!pane.getChildren().isEmpty())
    		pane.getChildren().remove(0);
    	
    	int j = 0;
    	przeciwnicy = new ImageView[36];
    	for(int i = 0; i<36; i++) {
    		przeciwnicy[i] = new ImageView();
    	    przeciwnicy[i].setImage(new Image("/application/inv.png"));
    	    przeciwnicy[i].setX((i%9)*40 + 200);
    	    if((i%9)==0)
    	    	j++;
    	    przeciwnicy[i].setY(j*40 + 10);
    	    pane.getChildren().add(przeciwnicy[i]);	
    	}
    	
    	statek.setX(x);
    	statek.setY(y);
    	pane.getChildren().add(statek);
    	new Thread(this).start();
    }
	
	/**	Instancja klasy Pocisk. W programie posiadam maksymalnie jedn� instancje klasy Pocisk w tej samej chwili.*/
    public Pocisk p;
    /**
     *  Funkcja wywo�ywana po wcisni�ciu przycisku odpowiadaj�cego za strza�.
     * 	Ma za zadanie stworzy� instancje klasy Pocisk i zapisa� j� w zmiennej p
     * 	oraz rozpocz�� dzia�anie w�tku rysuj�cego pocisk, lub je�li instancja zosta�a ju� stworzona,
     * 	a w�tek ju� dzia�a, wywo�a� funkcje ponownyStrzal() z klasy Pocisk, przekazuj�c jej obiekt kontrollera jako parametr.
     */
    public void strzal() {
    	if(pane.getChildren().size() < 38) { 
    		p = new Pocisk(this,pane);
    		new Thread(p).start();
    	}else{
    		p.ponowStrzal(this);
    	}
    }
    
    /**	Zmienna przechowuj�ca aktualny wynik.	*/
    public int wynik_int;
    /**
     *	Funkcja ta jest wywo�ywana przez obiekt klasy Pocisk.
     *	Zwi�ksza wynik gracza o 50 punkt�w.
     */
    public void nadpiszPunkty() {		
    	wynik_int += 50;
    	//String wynik = "" + wynik_int;
    	//punkty.setText(wynik);
    	//czyPrzeciwnikTrafiony[index] = true;
    }
    
    /**
     * Wyswietla okienko informuj�ce o wygranej.
     */
    public void wygrana() {
    	JFrame j = new JFrame("Gratuluje!");
    	j.setSize(300, 150);
    	j.setResizable(false);
    	j.setLocation(600, 400);
    	java.awt.Label l = new java.awt.Label("Wygrana. Tw�j wynik: " + wynik_int);
    	j.add(l);
    	j.setVisible(true);
    }
    
    /**
     * Wyswietla okienko informuj�ce o przegranej.
     */
    public void przegrana() {
    	JFrame j = new JFrame("Przegrana! :(");
    	j.setSize(300, 150);
    	j.setResizable(false);
    	j.setLocation(600, 400);
    	java.awt.Label l = new java.awt.Label("Kosmici dotarli za blisko ;__;");
    	j.add(l);
    	j.setVisible(true);
    }
    
    /** Flaga przechowuj�ca informacje o tym, czy powinni�my rysowa� przeciwnik�w w prawo w nast�pnym kroku.
     *	Gdy nie jest ustawiona, powinni�my poruszy� si� w lewo w kolejnym kroku.
     */
    public boolean rysujWPrawo = true;
    /**	Zmienna przechowuj�ca ilo�� rysowa� do wykonania w lewo lub prawo, zanim powinni�my zmieni� kierunek poruszania si� wrog�w.*/
    public int kroki;
    /**	Zmienna przechowuj�ca ilo�� rysowa� przeciwnik�w w d�. Na jej podstwie program okre�la, kiedy gracz przegrywa.*/
    public int kroki_dol;
    /**
     * Funkcja w�tku rysuj�cego przeciwnik�w.
     * W niesko�czonej p�tli sprawdza czy flaga endGame nie jest ustawiona
     * i rysuje przeciwnik�w w kierunku, kt�ry okre�laj� flagi prawo, lewo, d�, rysujWPrawo, oraz ilo�ci krok�w.
     * Zlicza tak�e ilo�� przeciwnik�w trafionych, korzystaj�c z tablicy czyPrzeciwnikTrafiony.
     * Wywo�uje funkcje wygrana(), gdy ilo�� trafionych przeciwnik�w jest r�wna ilo�ci wszystkich przeciwnik�w,
     * lub funkcje przegrana(), je�li zmienna kroki_dol osi�gn�a okre�lon� warto��
     * oraz ustawia flag� endGame.
     */
	@Override
	public void run() {
		Node n;
		int ileTrafionych = 0;
		kroki = 720;
		kroki_dol = 0;
		while(true) {
			if(endGame) {
				try {
					Thread.currentThread().join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}	
			}
			for(int i=0; i != 36; i++) {
				n = pane.getChildren().get(i);
						if(prawo) {
							if(kroki != 1440) {
								n.setLayoutX(n.getLayoutX()+10);
								kroki++;
							}else {
								n.setLayoutY(n.getLayoutY()+10);
								dol = true;
								prawo = false;
								rysujWPrawo = false;
							}
						}else if(lewo) {
							if(kroki != 0) {
								n.setLayoutX(n.getLayoutX()-10);
								kroki--;
							}else {
								n.setLayoutY(n.getLayoutY()+10);
								dol = true;
								lewo = false;
								rysujWPrawo = true;
							}
						}else if(dol) {
							if(kroki_dol == 1049) {
								n.setLayoutY(n.getLayoutY()+10);
								kroki_dol++;
								endGame = true;
								przegrana();
								p = null;
							}else {
								n.setLayoutY(n.getLayoutY()+10);
								kroki_dol++;
							}
						}
					
					if(czyPrzeciwnikTrafiony[i]) {
						n.setVisible(false);
						ileTrafionych++;
						if(ileTrafionych == 36) {
							endGame = true;
							wygrana();
							p = null;
						}
					}
			}
			ileTrafionych = 0;
			
			if(dol) {
				dol = false;
				if(rysujWPrawo)	
					prawo = true;
				else
					lewo = true;
			}

			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
