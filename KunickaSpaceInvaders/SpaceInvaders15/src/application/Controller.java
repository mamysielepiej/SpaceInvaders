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
*	Przechowuje zmienne, które s¹ pojemnikami na elementy sceny,
*	
*/
public class Controller implements Runnable{

    @FXML
    /**	Przycisk rozpoczynaj¹cy now¹ grê.	*/
    private Button nowaGra;

    @FXML
    /**	Etykieta, w której wyswietlony zostanie aktualny wynik.	*/
    private Label punkty;

    @FXML
    /**	Pojemnik w którym znajd¹ siê wszystkie elementy poruszaj¹ce siê po ekranie.	*/
    private Pane pane;
    
	    /**	Obiekt odtwarzaj¹cy muzykê.	*/
    	public MediaPlayer mp;
	    /**	Obiekt przechowuj¹cy wygl¹d statku.	*/
	    public ImageView statek;
	    /** Obiekt przechowuj¹cy wygl¹d pojedyñczego przeciwnika.	*/
	    public Image przeciwnikObraz;
	    /**	Tablica obrazów, w której przechowani zostan¹ przeciwnicy.	*/
	    public ImageView[] przeciwnicy;
	    /**	Tablica wartoœci prawda/fa³sz, w której przechowana zostanie informacja,
	     * 	czy przeciwnik o danym indeksie zosta³ trafiony.
	     */
	    public boolean[] czyPrzeciwnikTrafiony = new boolean[36];
	    /**	Wspó³rzêdne statku. */
	    public double x, y;
	    /**	Flaga przechowuj¹ca informacje,
		 *	w któr¹ stronê powinni poruszyæ siê przeciwnicy.
		 */
	    public boolean prawo, lewo, dol;
	    /**	Flaga która okreœla, czy gra w danej chwili trwa. */
	    public boolean endGame;
	
	/**
	 *	Funkcja ta uruchamia siê zaraz po konstruktorze klasy.
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
	 *	Dla klawisza S wywolac funkcje jesli gra nie jest zaczêta (flaga endGame nie jest ustawiona)
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
	 *	Funkcja ta zostaje wywolana po zamknieciu g³ównego okna.
	 *	Konczy dzialanie programu.
	 *	@param	t	zdarzenie zamkniecia okna
	 */
	public void close(WindowEvent t) {
		Platform.exit();
        System.exit(0);
	}
	
	/**
	 * 	Funkcja ta wywo³uje po kliknieicu w przycisk rozpoczynaj¹cy grê.
	 * 	Konczy dzialanie w¹tków poruszaj¹cych przeciwnikami i pociskiem.
	 * 	Ustawia wszystkie zmienne na wartoœci pocz¹tkowe.
	 * 	Inicializuje na nowo wszystkie elementy, które maj¹ wyœwietlaæ sie i poruszaæ w oknie podczas gry.
	 * 	Tworzy w¹tek poruszaj¹cy przeciwnikami.
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
	
	/**	Instancja klasy Pocisk. W programie posiadam maksymalnie jedn¹ instancje klasy Pocisk w tej samej chwili.*/
    public Pocisk p;
    /**
     *  Funkcja wywo³ywana po wcisniêciu przycisku odpowiadaj¹cego za strza³.
     * 	Ma za zadanie stworzyæ instancje klasy Pocisk i zapisaæ j¹ w zmiennej p
     * 	oraz rozpocz¹æ dzia³anie w¹tku rysuj¹cego pocisk, lub jeœli instancja zosta³a ju¿ stworzona,
     * 	a w¹tek ju¿ dzia³a, wywo³aæ funkcje ponownyStrzal() z klasy Pocisk, przekazuj¹c jej obiekt kontrollera jako parametr.
     */
    public void strzal() {
    	if(pane.getChildren().size() < 38) { 
    		p = new Pocisk(this,pane);
    		new Thread(p).start();
    	}else{
    		p.ponowStrzal(this);
    	}
    }
    
    /**	Zmienna przechowuj¹ca aktualny wynik.	*/
    public int wynik_int;
    /**
     *	Funkcja ta jest wywo³ywana przez obiekt klasy Pocisk.
     *	Zwiêksza wynik gracza o 50 punktów.
     */
    public void nadpiszPunkty() {		
    	wynik_int += 50;
    	//String wynik = "" + wynik_int;
    	//punkty.setText(wynik);
    	//czyPrzeciwnikTrafiony[index] = true;
    }
    
    /**
     * Wyswietla okienko informuj¹ce o wygranej.
     */
    public void wygrana() {
    	JFrame j = new JFrame("Gratuluje!");
    	j.setSize(300, 150);
    	j.setResizable(false);
    	j.setLocation(600, 400);
    	java.awt.Label l = new java.awt.Label("Wygrana. Twój wynik: " + wynik_int);
    	j.add(l);
    	j.setVisible(true);
    }
    
    /**
     * Wyswietla okienko informuj¹ce o przegranej.
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
    
    /** Flaga przechowuj¹ca informacje o tym, czy powinniœmy rysowaæ przeciwników w prawo w nastêpnym kroku.
     *	Gdy nie jest ustawiona, powinniœmy poruszyæ siê w lewo w kolejnym kroku.
     */
    public boolean rysujWPrawo = true;
    /**	Zmienna przechowuj¹ca iloœæ rysowañ do wykonania w lewo lub prawo, zanim powinniœmy zmieniæ kierunek poruszania siê wrogów.*/
    public int kroki;
    /**	Zmienna przechowuj¹ca iloœæ rysowañ przeciwników w dó³. Na jej podstwie program okreœla, kiedy gracz przegrywa.*/
    public int kroki_dol;
    /**
     * Funkcja w¹tku rysuj¹cego przeciwników.
     * W nieskoñczonej pêtli sprawdza czy flaga endGame nie jest ustawiona
     * i rysuje przeciwników w kierunku, który okreœlaj¹ flagi prawo, lewo, dó³, rysujWPrawo, oraz iloœci kroków.
     * Zlicza tak¿e iloœæ przeciwników trafionych, korzystaj¹c z tablicy czyPrzeciwnikTrafiony.
     * Wywo³uje funkcje wygrana(), gdy iloœæ trafionych przeciwników jest równa iloœci wszystkich przeciwników,
     * lub funkcje przegrana(), jeœli zmienna kroki_dol osi¹gnê³a okreœlon¹ wartoœæ
     * oraz ustawia flagê endGame.
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
