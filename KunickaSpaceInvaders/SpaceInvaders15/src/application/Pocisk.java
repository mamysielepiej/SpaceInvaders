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
	
	/** Instancja klasy kontrolera, na kt�rej pocisk b�dzie wywo�ywa� funkcje.	*/
	public Controller c;
	/** Wsp�rz�dna pocisku. */
	public double x, y;
	/** Pojemnik przechowuj�cy przeciwnik�w, statek i pocisk. */
	public Pane pane;
	/** Obiekt przechowuj�cy wygl�d pocisku. */
	public ImageView pocisk;

	/**
	 *	Konstruktor klasy Pocisk.
	 *	Ustawia pola w klasie.
	 *	@param c instancja klasy kontrolera
	 *	@param p pojemnik przechowuj�cy przeciwnik�w, statek i pocisk
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
	
	/**	Ustawia wsp�rz�dne pocisku dla kolejnego strza�u.	*/
	void ponowStrzal(Controller con) {
		x = con.x + 20;			
		y = con.y - 20;
		pane.getChildren().get(37).setLayoutY(y);		
		pane.getChildren().get(37).setLayoutX(x);
		pane.getChildren().get(37).setVisible(true); 	
	}
	
	/**
	 *	Funkcja sprawdzaj�ca, czy nast�pi�a kolizja.
	 *	Przechodzi w p�tli wszystkie elementy z indeksami 0-35 (wrog�w),
	 *	pobieraj�c ich wsp�rz�dne i sprawdzaj�c w funckji czyZachodzaNaSiebie(), 
	 *	je�li wr�g nie by� trafiony, czy nast�pi�a kolizja.
	 *	Je�li nast�pi�a picisk jest na niewidoczny i przesuwany poza ekran, �eby nie spowodowa� nast�pnych kolizji,
	 *	oraz ustawiana jest flaga w tablicy czyPrzeciwnikTrafiony pod indeksem tego przeciwnika i
	 *	wywo�ana funkcja nadpiszPunkty() z klasy kontrolera.
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
	 *	Sprawdza czy wsp�rz�dne pocisku znajduj� si� w obr�bie kwadratu
	 *	o boku d�ugo�ci r�wnym d�ugo�ci boku obrazka wroga, kt�rego lewym g�rnym wierzcho�kiem
	 *	jest wierzcho�ek o wsp�rz�dnych xp i yp.
	 *	@param xp wsp�rz�dna x wroga
	 *	@param yp wsp�rz�dna y wroga
	 *	@return true gdy warunek spe�niony
	 */
	private boolean czyZachodzaNaSiebie(double xp, double yp) {
		if(y>yp && y<yp+37 && x>xp && x<xp+37)
			return true;
		return false;
	}

	/**
	 *	Funkcja w�tku rysuj�cego pocisk.
	 *	W niesko�czonej p�tli ustawia nowe wsp�rz�dne pocisku i sprawdza kolizje co pewien okre�lony odst�p czasu,
	 *	sprawdzaj�c czy flaga endGame nie jest ustawiona.
	 *	W�tek ko�czy prac�, gdy flaga endGame jest ustawiona.
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
