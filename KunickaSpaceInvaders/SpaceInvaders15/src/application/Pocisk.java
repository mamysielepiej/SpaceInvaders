package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 * 	Klasa wyswietlajaca pocisk i poruszajaca nim.
 * 	Przechowuje parametry potrzebne do rysowania pocisku w odpowiednim miejscu w oknie, porusza nim
 * 	i wykrywa kolizje z innymi elementami narysowanymi w oknie.
 *
 */
public class Pocisk implements Runnable{
	
	/** Instancja klasy kontrolera, na której pocisk bêdzie wywo³ywa³ funkcje.	*/
	public Controller c;
	/** Wspó³rzêdna pocisku. */
	public double x, y;
	/** Pojemnik przechowuj¹cy przeciwników, statek i pocisk. */
	public Pane pane;
	/** Obiekt przechowuj¹cy wygl¹d pocisku. */
	public ImageView pocisk;

	/**
	 *	Konstruktor klasy Pocisk.
	 *	Ustawia pola w klasie.
	 *	@param c instancja klasy kontrolera
	 *	@param p pojemnik przechowuj¹cy przeciwników, statek i pocisk
	 */
	Pocisk(Controller c, Pane p) {
		this.c = c;
		this.x = c.x + 20;
		this.y = c.y - 20;
		this.pane = p;
		pocisk = new ImageView(new Image("/application/pocisk.png"));
    	pocisk.setLayoutX(x);
    	pocisk.setLayoutY(y-10);
    	pane.getChildren().add(pocisk);
	}	
	
	/**	Ustawia wspó³rzêdne pocisku dla kolejnego strza³u.	*/
	void ponowStrzal(Controller con) {
		x = con.x + 20;			
		y = con.y - 20;
		pane.getChildren().get(37).setLayoutY(y);		
		pane.getChildren().get(37).setLayoutX(x);
		pane.getChildren().get(37).setVisible(true); 	
	}
	
	/**
	 *	Funkcja sprawdzaj¹ca, czy nast¹pi³a kolizja.
	 *	Przechodzi w pêtli wszystkie elementy z indeksami 0-35 (wrogów),
	 *	pobieraj¹c ich wspó³rzêdne i sprawdzaj¹c w funckji czyZachodzaNaSiebie(), 
	 *	jeœli wróg nie by³ trafiony, czy nast¹pi³a kolizja.
	 *	Jeœli nast¹pi³a picisk jest na niewidoczny i przesuwany poza ekran, ¿eby nie spowodowaæ nastêpnych kolizji,
	 *	oraz ustawiana jest flaga w tablicy czyPrzeciwnikTrafiony pod indeksem tego przeciwnika i
	 *	wywo³ana funkcja nadpiszPunkty() z klasy kontrolera.
	 */
	public void kolizja() {
		double xp = 0, yp = 0;
		int j = 0;
		for(int i = 0; i < 36; i++) {
			if(i%9==0)
				j++;
			if(pane.getChildren().get(i).isVisible()) {
				xp = pane.getChildren().get(i).getLayoutX();
				yp = pane.getChildren().get(i).getLayoutY();				
				xp = (i%9)*40 +200 +xp;
				yp = j*40 +10 +yp;
				if(czyZachodzaNaSiebie(xp,yp)) {
					pane.getChildren().get(37).setLayoutY(0);
					pane.getChildren().get(37).setVisible(false);
					c.nadpiszPunkty();
					c.czyPrzeciwnikTrafiony[i] = true;
					i = 36;
				}
			}
		}
	}
	
	/**
	 *	Sprawdza czy wspó³rzêdne pocisku znajduj¹ siê w obrêbie kwadratu
	 *	o boku d³ugoœci równym d³ugoœci boku obrazka wroga, którego lewym górnym wierzcho³kiem
	 *	jest wierzcho³ek o wspó³rzêdnych xp i yp.
	 *	@param xp wspó³rzêdna x wroga
	 *	@param yp wspó³rzêdna y wroga
	 *	@return true gdy warunek spe³niony
	 */
	private boolean czyZachodzaNaSiebie(double xp, double yp) {
		if(y>yp && y<yp+37 && x>xp && x<xp+37)
			return true;
		return false;
	}

	/**
	 *	Funkcja w¹tku rysuj¹cego pocisk.
	 *	W nieskoñczonej pêtli ustawia nowe wspó³rzêdne pocisku i sprawdza kolizje co pewien okreœlony odstêp czasu,
	 *	sprawdzaj¹c czy flaga endGame nie jest ustawiona.
	 *	W¹tek koñczy pracê, gdy flaga endGame jest ustawiona.
	 */
	@Override
	public void run() {	//bede zmniejszal y (pocisk bedzie lecial w gore), sprawdzal kolizje i zasypial na chwile, zeby powtorzyc znowu wszystko jeszcze raz
		while(true) {
	
			//wyliczam nowa wartosc y
			y = pane.getChildren().get(37).getLayoutY() - 10;
			
			//kolejna linijka ustawi zaktualizowane y
			if(y > 0)
				pane.getChildren().get(37).setLayoutY(y);
			else
				pane.getChildren().get(37).setVisible(false);
			
			//sprawdzam, czy w nic nie wszedlem ;>
			kolizja();
			
			if(c.endGame) {
				try {
					Thread.currentThread().join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}				
			
			//jesli nie trafilismy i nie wyszlismy za ekran, to idz spac i powtorz w nieskonczonej petli
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
