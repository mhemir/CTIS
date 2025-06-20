/*********************************
CTIS-164 Homework-1

NAME : MEHMET EMÝR
SURNAME : YILMAZ
ID : 22202966
Section : 04

Project : Ankara Metro Simulation

Additional Features:
-When left button of mouse clicked, time will be set to night or noon
-In the noon, it can generate random positioned clouds every time
-When the animation starts metro visits M1, M2 and M3 lines and their stops one-by-one until it reachs to the last stop which is "KORU"

*********************************/
#ifdef _MSC_VER
#define _CRT_SECURE_NO_WARNINGS
#endif
#include <GL/glut.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include <stdarg.h>
#include <time.h>

#define WINDOW_WIDTH 1280
#define WINDOW_HEIGHT 960
#define TIMER_PERIOD 20 // Period for the timer.
#define TIMER_ON 1 // 0:disable timer, 1:enable timer
#define D2R 0.01745329252
#define PI 3.14159265358
#define MAX_SPEED  20
#define MIN_SPEED  5
/* Global Variables */
int winWidth = WINDOW_WIDTH, winHeight = WINDOW_HEIGHT; // Set current window width and height
bool activeTimer = false;
bool night = true;
int speed = 5;
double wallSquareSize = 40;
double wallSquareConstantFac = 0.8;
double innerWindowTickness = 40;
double innerWindowPosX = -500;
double innerWindowPosY = -300;
float initialTrainPosX = innerWindowPosX; // Set the starting x position of train
float currentTrainPosX = initialTrainPosX;
float currentTrainPosY = -91;
char stops[][30] = { "OSB-TOREKENT", "GOP", "FATIH", "HARIKALAR DIYARI", "ERYAMAN 5", "ERYAMAN 1 - 2", "ISTANBUL YOLU", "BOTANIK", "MESA", "BATI MERKEZ", "BATIKENT" ,"OSTIM", "MACUNKOY", "HASTANE", "DEMETEVLER","YENIMAHALLE","IVEDIK","AKKOPRU","ATATURK KULTUR MERKEZI", "ULUS", "SIHHIYE", "KIZILAY", "NECATIBEY","MILLI KUTUPHANE", "SOGUTOZU","MTA","ODTU", "* BILKENT *", "TARIM BAKANLIGI - DANISTAY", "BEYTEPE", "UMITKOY" ,"CAYYOLU" ,"KORU" };
int initialStopIndex = 0;
int currentStopIndex = initialStopIndex;
float initialScenePos = 250;
float currentScenePos = initialScenePos;


// To draw a filled circle, centered at (x,y) with radius r
void circle(int x, int y, int r) {
	float angle;
	glBegin(GL_POLYGON);
	for (int i = 0; i < 50; i++) {
		angle = 2 * PI * i / 50;
		glVertex2f(x + r * cos(angle), y + r * sin(angle));
	}
	glEnd();
}
// Display text with variables

void vprint(int x, int y, void* font, const char* string, ...) {
	va_list ap;
	va_start(ap, string);
	char str[1024];
	vsprintf_s(str, string, ap);
	va_end(ap);

	int len, i;
	glRasterPos2f(x, y);
	len = (int)strlen(str);
	for (i = 0; i < len; i++)
		glutBitmapCharacter(font, str[i]);
}

void drawWindow() {

	glLineWidth(3);

	glColor3ub(91, 96, 101);
	glBegin(GL_QUADS);

	//LEFT
	glVertex2f(innerWindowPosX, -innerWindowPosY);
	glVertex2f(innerWindowPosX, innerWindowPosY);
	glVertex2f(-(-innerWindowPosX + innerWindowTickness), -(-innerWindowPosY + innerWindowTickness));
	glVertex2f(-(-innerWindowPosX + innerWindowTickness), -innerWindowPosY + innerWindowTickness);

	//RIGHT
	glVertex2f(-innerWindowPosX + innerWindowTickness, -innerWindowPosY + innerWindowTickness);
	glVertex2f(-innerWindowPosX + innerWindowTickness, -(-innerWindowPosY + innerWindowTickness));
	glVertex2f(-innerWindowPosX, innerWindowPosY);
	glVertex2f(-innerWindowPosX, -innerWindowPosY);

	//UP
	glVertex2f(-innerWindowPosX, -innerWindowPosY);
	glVertex2f(innerWindowPosX, -innerWindowPosY);
	glVertex2f(-(-innerWindowPosX + innerWindowTickness), -innerWindowPosY + innerWindowTickness);
	glVertex2f(-innerWindowPosX + innerWindowTickness, -innerWindowPosY + innerWindowTickness);

	//DOWN
	glVertex2f(innerWindowPosX, innerWindowPosY);
	glVertex2f(-(-innerWindowPosX + innerWindowTickness), -(-innerWindowPosY + innerWindowTickness));
	glVertex2f(-innerWindowPosX + innerWindowTickness, -(-innerWindowPosY + innerWindowTickness));
	glVertex2f(-innerWindowPosX, innerWindowPosY);

	glEnd();

	glColor3ub(0, 0, 0);


	//Handle
	circle(-200, innerWindowPosY - 20, 5);
	glBegin(GL_LINES);
	glVertex2f(-200, innerWindowPosY - 20);
	glVertex2f(200, innerWindowPosY - 20);
	glEnd();
	circle(200, innerWindowPosY - 20, 5);


	//Outermost boundary
	glBegin(GL_LINE_LOOP);
	glVertex2f(-(-innerWindowPosX + innerWindowTickness), -innerWindowPosY + innerWindowTickness);
	glVertex2f(-(-innerWindowPosX + innerWindowTickness), -(-innerWindowPosY + innerWindowTickness));
	glVertex2f(-innerWindowPosX + innerWindowTickness, -(-innerWindowPosY + innerWindowTickness));
	glVertex2f(-innerWindowPosX + innerWindowTickness, -innerWindowPosY + innerWindowTickness);
	glEnd();

	//Inner boundary
	glBegin(GL_LINE_LOOP);
	glVertex2f(-innerWindowPosX, -innerWindowPosY);
	glVertex2f(innerWindowPosX, -innerWindowPosY);
	glVertex2f(innerWindowPosX, innerWindowPosY);
	glVertex2f(-innerWindowPosX, innerWindowPosY);
	glEnd();

	glColor3ub(0, 0, 0);


}

void drawSpecialSquare(int x, int y) {
	glColor3ub(200, 200, 200);
	double size = wallSquareSize;
	double constant = wallSquareConstantFac;

	//Upper half
	glBegin(GL_POLYGON);
	glVertex2f(x, y);
	glVertex2f(x + size, y);
	glVertex2f(x + size * constant, y - (size - size * constant));
	glVertex2f(x + (size - size * constant), y - (size - size * constant));
	glVertex2f(x + (size - size * constant), y - size * constant);
	glVertex2f(x, y - size);
	glEnd();

	//Lower half
	glColor3ub(128, 128, 128);
	glBegin(GL_POLYGON);
	glVertex2f(x + size, y - size); //1
	glVertex2f(x, y - size);
	glVertex2f(x + (size - size * constant), y - (size * constant));
	glVertex2f(x + (size * constant), y - (size * constant));
	glVertex2f(x + (size * constant), y - (size - size * constant));
	glVertex2f(x + size, y);
	glEnd();

	glColor3ub(181, 184, 177);
	glBegin(GL_QUADS);
	glVertex2f(x + (size - size * constant), y - (size - size * constant));
	glVertex2f(x + (size * constant), y - (size - size * constant));
	glVertex2f(x + (size * constant), y - (size * constant));
	glVertex2f(x + (size - size * constant), y - (size * constant));

	glEnd();
}




void drawMask() {
	//Right mask
	glColor3ub(226, 218, 196);
	glBegin(GL_QUADS);
	glVertex2f(innerWindowPosX - innerWindowTickness - 2, innerWindowPosY - innerWindowTickness - 2);
	glColor3ub(100, 218, 196);
	glVertex2f(-winWidth / 2, innerWindowPosY - innerWindowTickness);
	glVertex2f(-winWidth / 2, -(innerWindowPosY - innerWindowTickness));
	glVertex2f(innerWindowPosX - innerWindowTickness - 2, -(innerWindowPosY - innerWindowTickness));
	glEnd();

	glColor3ub(100, 218, 196);
	//Left mask
	glBegin(GL_QUADS);
	glColor3ub(226, 218, 196);
	glVertex2f(-innerWindowPosX + innerWindowTickness + 1, innerWindowPosY - innerWindowTickness - 1);
	glVertex2f(+winWidth / 2, innerWindowPosY - innerWindowTickness);
	glColor3ub(100, 218, 196);
	glVertex2f(+winWidth / 2, -(innerWindowPosY - innerWindowTickness));
	glVertex2f(-innerWindowPosX + innerWindowTickness + 1, -(innerWindowPosY - innerWindowTickness));
	glEnd();

	glLineWidth(4);





}

void drawWallPattern2() {
	glLineWidth(4);
	glBegin(GL_LINES);
	// Gray lines horizontally
	glColor3ub(96, 96, 96);
	for (int i = -winHeight / 2; i <= winHeight / 2; i += 50) {
		glVertex2f(-winWidth / 2, i);
		glVertex2f(winWidth / 2, i);
	}
	glEnd();
}




void drawWallPattern() {
	glLineWidth(3);
	/*for (int i = -winWidth / 2; i <= winWidth / 2 - wallSquareSize; i += wallSquareSize) {
		for (int j = winHeight / 2; j >= -winHeight / 2 + wallSquareSize; j -= wallSquareSize)*/
	int rowBegin = 0;
	int rowEnd = winHeight / wallSquareSize - 1;
	int colBegin = 0;
	int colEnd = winWidth / wallSquareSize - 1;


	while (rowBegin <= rowEnd && colBegin <= colEnd) {
		// Traverse Right
		for (int j = colBegin; j <= colEnd; j++) {
			//res.add(matrix[rowBegin][j]);
			drawSpecialSquare(-(winWidth / 2) + j * wallSquareSize, winHeight / 2 - rowBegin * wallSquareSize);
		}
		rowBegin++;

		// Traverse Down
		for (int j = rowBegin; j <= rowEnd; j++) {
			//res.add(matrix[j][colEnd]);
			drawSpecialSquare(colEnd * wallSquareSize - winWidth / 2, winHeight / 2 - j * wallSquareSize);
		}
		colEnd--;

		if (rowBegin <= rowEnd) {
			// Traverse Left
			for (int j = colEnd; j >= colBegin; j--) {
				//res.add(matrix[rowEnd][j]);
				drawSpecialSquare(j * wallSquareSize - winWidth / 2, winHeight / 2 - rowEnd * wallSquareSize);
			}
		}
		rowEnd--;

		if (colBegin <= colEnd) {
			// Traverse Up
			for (int j = rowEnd; j >= rowBegin; j--) {
				//res.add(matrix[j][colBegin]);
				drawSpecialSquare(colBegin * wallSquareSize - winWidth / 2, winHeight / 2 - j * wallSquareSize);

			}
		}
		colBegin++;
	}
}

void drawTrain(int x, int y) {
	//Provide easier usage of variables.
	//relX and relY stand for (0,0) relative coordinates
	//Based on bottom-left corner of train, which is origin.
	int relX = x + 400, relY = y;

	//Draw the body of train
	int arcStartX = relX + 37;
	int arcStartY = relY + 51;
	glColor3ub(255, 255, 255);
	glBegin(GL_POLYGON);
	glVertex2f(relX - 400, relY);
	glVertex2f(relX - 400, relY + 60);
	glColor3ub(192, 192, 192);
	glVertex2f(relX + 30, relY + 60);
	//Drawing arc, approximate values were used.
	glVertex2f(arcStartX, arcStartY);
	glVertex2f(arcStartX + 4, arcStartY - 8);
	glVertex2f(arcStartX + 8, arcStartY - 16);
	glVertex2f(arcStartX + 11, arcStartY - 28);
	glVertex2f(arcStartX + 12, arcStartY - 36);
	glVertex2f(arcStartX + 12.5, arcStartY - 43.5);
	glColor3ub(161, 171, 178);
	glVertex2f(relX + 50, relY + 2.5);
	glVertex2f(relX + 50, relY);
	glEnd();

	glColor3ub(0, 0, 0); //Outermost border color
	glLineWidth(3);
	glBegin(GL_LINE_LOOP);
	glVertex2f(relX - 400, relY);
	glVertex2f(relX - 400, relY + 60);
	glVertex2f(relX + 30, relY + 60);
	//Drawing arc, approximate values were used.
	glVertex2f(arcStartX, arcStartY);
	glVertex2f(arcStartX + 4, arcStartY - 8);
	glVertex2f(arcStartX + 8, arcStartY - 16);
	glVertex2f(arcStartX + 11, arcStartY - 28);
	glVertex2f(arcStartX + 12, arcStartY - 36);
	glVertex2f(arcStartX + 12.5, arcStartY - 43.5);

	glVertex2f(relX + 50, relY + 2.5);
	glVertex2f(relX + 50, relY);
	glEnd();



	glLineWidth(1);

	glBegin(GL_LINES);
	glVertex2f(relX + 50, relY + 2.5);
	glVertex2f(relX - 400, relY + 2.5);
	glEnd();

	glColor3ub(0, 0, 0); //Outermost border color

	//Draw the doors

	//Driver's door
	glBegin(GL_LINE_LOOP);
	glVertex2f(relX + 15.625, relY + 9.5);
	glVertex2f(relX + 30.625, relY + 9.5);
	glVertex2f(relX + 30.625, relY + 40.8);
	glVertex2f(relX + 25.625, relY + 49.5);
	glVertex2f(relX + 15.625, relY + 49.5);
	glEnd();
	//And its window
	glBegin(GL_QUADS);
	glColor3ub(128, 141, 149);
	glVertex2f(relX + 26, relY + 44.5);
	glColor3ub(0, 0, 0);
	glVertex2f(relX + 26, relY + 27);
	glVertex2f(relX + 18.5, relY + 27);
	glVertex2f(relX + 18.5, relY + 44.5);
	glEnd();

	//Passengers' doors
	for (int i = 0; i < 4; i++) {

		glColor3ub(116, 14, 25); //Red color
		glBegin(GL_QUADS);
		glVertex2f(relX - (1.875 + i * 110.625), relY + 7.5);
		glColor3ub(95, 15, 22); //Red color
		glVertex2f(relX - (39.375 + i * 110.625), relY + 7.5);
		glVertex2f(relX - (39.375 + i * 110.625), relY + 57.5);
		glVertex2f(relX - (1.875 + i * 110.625), relY + 57.5);
		glEnd();
		glColor3ub(0, 0, 0);
		//Half line of doors
		glBegin(GL_LINES);
		glVertex2f(relX - (20.625 + i * 110.625), relY + 57.5);
		glVertex2f(relX - (20.625 + i * 110.625), relY + 7.5);
		glEnd();

		//Windows of doors
		for (int j = 0; j < 2; j++) {
			glBegin(GL_QUADS);
			glColor3ub(128, 141, 149);
			glVertex2f(relX - ((6.25 + i * 110.625) + j * 18.75), relY + 50);
			glColor3ub(0, 0, 0);
			glVertex2f(relX - ((6.25 + i * 110.625) + j * 18.75), relY + 30);
			glVertex2f(relX - ((16.25 + i * 110.625) + j * 18.75), relY + 30);
			glVertex2f(relX - ((16.25 + i * 110.625) + j * 18.75), relY + 50);
			glEnd();
		}
		//Lights of doors
		circle(relX - (23.5 + i * 110.625), relY + 37.8, 1.5);


	}

	//Train's Windows
	for (int i = 0; i < 3; i++) {
		//According to the pattern, there is one air filter at top of windows
		glBegin(GL_QUADS);
		glVertex2f(relX - (53.75 + i * 110.625), relY + 52);
		glVertex2f(relX - (58.25 + i * 110.625), relY + 52);
		glVertex2f(relX - (58.75 + i * 110.625), relY + 56.5);
		glVertex2f(relX - (53.75 + i * 110.625), relY + 56.5);
		glEnd();

		//Window pattern
		for (int j = 0; j < 2; j++) {
			glBegin(GL_QUADS);
			glColor3ub(128, 141, 149);
			glVertex2f(relX - ((73.75 + i * 110.625) + j * 24.375), relY + 50);
			glColor3ub(0, 0, 0);
			glVertex2f(relX - ((73.75 + i * 110.625) + j * 24.375), relY + 30);
			glVertex2f(relX - ((53.75 + i * 110.625) + j * 24.375), relY + 30);
			glVertex2f(relX - ((53.75 + i * 110.625) + j * 24.375), relY + 50);
			glEnd();
		}
	}

	//Large Air filters
	for (int i = 0; i < 2; i++) {
		for (int j = 0; j < 2; j++) {
			glBegin(GL_QUADS);
			glVertex2f(relX - ((77.125 + 2 * i * 110.625) + j * 12), relY + 53);
			glVertex2f(relX - ((87.125 + 2 * i * 110.625) + j * 12), relY + 53);
			glVertex2f(relX - ((87.125 + 2 * i * 110.625) + j * 12), relY + 57.5);
			glVertex2f(relX - ((77.125 + 2 * i * 110.625) + j * 12), relY + 57.5);
			glEnd();
		}
	}

	//Last air filter
	glBegin(GL_QUADS);
	glVertex2f(relX - 383.25, relY + 52.5);
	glVertex2f(relX - 388.25, relY + 52.5);
	glVertex2f(relX - 388.25, relY + 57.5);
	glVertex2f(relX - 383.25, relY + 57.5);
	glEnd();


	//Text Area
	glBegin(GL_LINE_LOOP);
	glVertex2f(relX - 188.25, relY + 54);
	glVertex2f(relX - 209.25, relY + 54);
	glVertex2f(relX - 209.25, relY + 56.5);
	glVertex2f(relX - 188.25, relY + 56.5);
	glEnd();
	//I wanted to write a text but even the minimum size of font size didn't fit in this area.
	// vprint(relX-209.25, relY+52, GLUT_BITMAP_HELVETICA_10, "KORU");

	//Inner lines
	glBegin(GL_LINE_STRIP);
	glVertex2f(relX + 15.625, relY + 60);
	glVertex2f(relX + 15.625, relY + 56.5);
	glVertex2f(relX + 27.125, relY + 56.5);
	glVertex2f(relX + 34.625, relY + 43.5);
	glVertex2f(relX + 34.625, relY + 5);
	glVertex2f(relX + 35.625, relY + 5);
	glVertex2f(relX + 35.625, relY + 2.5);
	glEnd();

	glBegin(GL_LINES);
	glVertex2f(relX + 34.625, relY + 7.5);
	glVertex2f(relX + 50, relY + 7.5);
	glVertex2f(relX + 30, relY + 51.5);
	glVertex2f(relX - 1.875, relY + 51.5);
	glEnd();

	//Middle lines
	for (int i = 0; i < 3; i++) {

		for (int j = 0; j < 2; j++) {
			glBegin(GL_LINES);
			glVertex2f(relX - (39.375 + i * 110.625), relY + 51.5 - j * 23);
			glVertex2f(relX - (112.5 + i * 110.625), relY + 51.5 - j * 23);
			glEnd();
		}
	}
	//Line at the very right side
	glBegin(GL_LINES);
	glVertex2f(relX - 1.875, relY + 28.5);
	glVertex2f(relX + 15.625, relY + 28.5);
	glVertex2f(relX + 30.625, relY + 28.5);
	glVertex2f(relX + 34.625, relY + 28.5);
	glEnd();


	//Line at the very left side
	glBegin(GL_LINES);
	glVertex2f(relX - 371.25, relY + 51.5);
	glVertex2f(relX - 400, relY + 51.5);
	glVertex2f(relX - 371.25, relY + 28.5);
	glVertex2f(relX - 400, relY + 28.5);
	glEnd();

}

void drawGroundPerspective() {
	glLineWidth(1);
	//Ground Color
	glColor3ub(192, 192, 192);
	glBegin(GL_QUADS);
	glVertex2f(innerWindowPosX, -91);
	glVertex2f(-innerWindowPosX, -91);
	glVertex2f(-innerWindowPosX, -300);
	glColor3ub(34, 34, 34);
	glVertex2f(innerWindowPosX, -300);
	glEnd();


	glColor3ub(0, 0, 0);
	glLineWidth(3);
	//Draw horizontal lines
	glBegin(GL_LINES);
	for (int i = 1; i <= 13; i++) {
		//printf("%lf", -(40.18 * sinh(0.2244 * i - 0.6768) + 110.26));
		glVertex2f(innerWindowPosX, -(40.18 * sinh(0.2244 * i - 0.6768) + 110.26));
		glVertex2f(-innerWindowPosX, -(40.18 * sinh(0.2244 * i - 0.6768) + 110.26));
	}
	glEnd();

	//Draw vertical lines 
	glBegin(GL_LINES);
	for (int i = 0; i < 14; i += 1) {
		//Left side
		glVertex2f(innerWindowPosX + 75 * i, -91);
		glVertex2f(innerWindowPosX, -(40.18 * sinh(0.2244 * i - 0.6768) + 110.26));

		//Right side
		glVertex2f(-innerWindowPosX - 75 * i, -91);
		glVertex2f(-innerWindowPosX, -(40.18 * sinh(0.2244 * i - 0.6768) + 110.26));

	}
	glEnd();


}

void drawStopLabel(int stopNo) {

	//Draw handles
	glColor3ub(0, 0, 0);
	glBegin(GL_LINES);
	glLineWidth(10);
	for (int i = 0; i < 2; i++) {
		glVertex2f(-125 + i * 250, 300);
		glVertex2f(-125 + i * 250, 275);
	}
	glEnd();

	//Draw pane
	glBegin(GL_QUADS);
	glLineWidth(3);
	glColor3ub(255, 0, 0);
	glVertex2f(-150, 275);
	glVertex2f(-150, 240);
	glVertex2f(+150, 240);
	glVertex2f(+150, 275);
	glEnd();
	glColor3ub(255, 255, 255);

	//Print stop data
	vprint(-125 + (125 - (strlen(stops[stopNo]) / 2) * 11.5), 250, GLUT_BITMAP_HELVETICA_18, stops[stopNo]);

}

void drawCloud(int x, int y) {
	glColor3f(1, 1, 1); // White
	circle(0 + x, 0 + y, 30);
	circle(-25 + x, y, 20);
	circle(25 + x, -2 + y, 20);
	circle(21 + x, -19 + y, 10);
}


int randNumber(int min, int max) {
	return min + rand() % (max - min + 1);
}


void drawScene(double pos) {

	if (night) {
		//Sun
		glColor3ub(255, 255, 0);
		circle(pos - 700, 250, 30);

		//Clouds
		glColor3ub(255, 255, 255);


		srand(time(NULL));

		for (int i = 0; i < 10; i++) {
			int x = randNumber(0, 900); // x is between 0 and 900 
			int y = randNumber(250, 275); // y is between 250 and 275

			drawCloud(pos - x, y);
		}
	}
	else {
		//Moon
		glColor3ub(255, 255, 255);
		circle(pos - 700, 250, 30);
		glColor3ub(4, 26, 64);
		circle(pos - 675, 270, 50);




	}

}


void displayName() {
	glColor3ub(190, 80, 96);
	glLineWidth(3);
	glBegin(GL_QUADS);
	glVertex2f(-300, (winHeight + innerWindowPosY + innerWindowTickness) / 2 + 20);
	glVertex2f(-300, (winHeight + innerWindowPosY + innerWindowTickness) / 2 + 60);
	glVertex2f(300, (winHeight + innerWindowPosY + innerWindowTickness) / 2 + 60);
	glVertex2f(300, (winHeight + innerWindowPosY + innerWindowTickness) / 2 + 20);
	glEnd();
	//Its border
	glColor3ub(0, 0, 0);
	glLineWidth(3);
	glBegin(GL_LINE_LOOP);
	glVertex2f(-300, (winHeight + innerWindowPosY + innerWindowTickness) / 2 + 20);
	glVertex2f(-300, (winHeight + innerWindowPosY + innerWindowTickness) / 2 + 60);
	glVertex2f(300, (winHeight + innerWindowPosY + innerWindowTickness) / 2 + 60);
	glVertex2f(300, (winHeight + innerWindowPosY + innerWindowTickness) / 2 + 20);
	glEnd();



	glBegin(GL_LINES);
	glVertex2f(-300, (winHeight + innerWindowPosY + innerWindowTickness) / 2 + 60);
	glVertex2f(0, (winHeight + innerWindowPosY + innerWindowTickness) / 2 + 90);
	glVertex2f(300, (winHeight + innerWindowPosY + innerWindowTickness) / 2 + 60);
	glVertex2f(0, (winHeight + innerWindowPosY + innerWindowTickness) / 2 + 90);
	glEnd();

	glColor3ub(92, 92, 92);
	circle(0, (winHeight + innerWindowPosY + innerWindowTickness) / 2 + 90, 10);

	glColor3ub(255, 255, 255);
	vprint(-100, (winHeight + innerWindowPosY + innerWindowTickness) / 2 + 40, GLUT_BITMAP_HELVETICA_18, "BY MEHMET EMIR YILMAZ");

}


void info() {
	glColor3ub(0, 0, 0);
	vprint(-winWidth / 2 + 10, winHeight / 2 - 20, GLUT_BITMAP_HELVETICA_18, "Press F1 to start animation. STOP/RESUME: Spacebar(toogle)");
	vprint(-winWidth / 2 + 10, winHeight / 2 - 40, GLUT_BITMAP_HELVETICA_18, "Click the left button of mouse to change time (Noon/Night)");
}

// To display onto window using OpenGL commands
void display() {
	glClearColor(1 * (226.0 / 256), 1 * (218.0 / 256), 1 * (196.0 / 256), 0);
	glClear(GL_COLOR_BUFFER_BIT);

	glColor3ub(226, 218, 196);
	drawWallPattern();

	if (night) {
		glColor3ub(0, 255, 255); //Turqoise color for noon
	}
	else {
		glColor3ub(4, 26, 64); //Dark blue color for night

	}
	glRectf(-innerWindowPosX, -innerWindowPosY, innerWindowPosX, innerWindowPosY); // Window Area
	//glColor3ub(0, 0, 0);
	drawTrain(currentTrainPosX, currentTrainPosY);
	drawGroundPerspective();
	drawScene(currentScenePos);
	drawStopLabel(currentStopIndex);
	drawWindow();

	drawMask();
	displayName();
	info();
	glColor3ub(255, 255, 255);
	/*glBegin(GL_LINES);
	glVertex2f(-119, 248);
	glVertex2f(-105, 248);
	glEnd();*/
	glutSwapBuffers();

}

// This function is called when the window size changes.
// w : is the new width of the window in pixels.
// h : is the new height of the window in pixels.
void onResize(int w, int h) {
	winWidth = w;
	winHeight = h;
	glViewport(0, 0, w, h);
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();
	glOrtho(-w / 2, w / 2, -h / 2, h / 2, -1, 1);
	glMatrixMode(GL_MODELVIEW);
	//glLoadIdentity();
	display(); // Refresh window.

}


void toggleMotion(unsigned char key, int x, int y) {
	if (key == 27) {
		exit(0);
	}
	else if (key == ' ') {
		activeTimer = !activeTimer;
		printf("%d", activeTimer);
	}
	glutPostRedisplay();
}

//void adjustSpeed(int key, int x, int y) {
//	
//	switch (key) {
//	case GLUT_KEY_LEFT:
//		printf("left");
//		if (speed >= MIN_SPEED) { 
//			speed--;
//			glutPostRedisplay();
//		}
//		break;
//	case GLUT_KEY_RIGHT:
//		printf("right");
//		if (speed <= MAX_SPEED) { 
//			speed++;
//			glutPostRedisplay();
//		}
//		break;
//	}
//	// To refresh the window it calls display() function
//}


void onSpecialKeyDown(int key, int x, int y) {
	if (key == GLUT_KEY_F1) {
		activeTimer = true;
		currentTrainPosX = initialTrainPosX;
		currentScenePos = initialScenePos;
	}
	glutPostRedisplay();
}

void onClick(int button, int state, int x, int y) {

	if (button == GLUT_LEFT_BUTTON && state == GLUT_DOWN) {
		night = !night;
	}

}


#if TIMER_ON == 1
void onTimer(int v) {
	glutTimerFunc(TIMER_PERIOD, onTimer, 0);

	if (activeTimer) {
		currentTrainPosX += speed;
		currentScenePos -= speed;
		if (currentTrainPosX > winWidth / 2) {
			currentTrainPosX = innerWindowPosX - 400;
			currentStopIndex++;

		}
		if (currentScenePos < -winWidth / 2) {
			currentScenePos = winWidth;
		}
	}
	// To refresh the window it calls display() function
	glutPostRedisplay(); //display()
}
#endif



int main(int argc, char* argv[]) {
	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE);
	glutInitWindowSize(WINDOW_WIDTH, WINDOW_HEIGHT);
	glutInitWindowPosition(600, 200);
	glutCreateWindow("MEHMET EMIR YILMAZ - ANKARA METRO");


#if TIMER_ON == 1
	glutTimerFunc(TIMER_PERIOD, onTimer, 0);
#endif


	// Window Events
	glutDisplayFunc(display);
	glutReshapeFunc(onResize);
	glutKeyboardFunc(toggleMotion);
	//glutSpecialFunc(adjustSpeed);
	glutSpecialFunc(onSpecialKeyDown);
	glutMouseFunc(onClick);


	glutMainLoop();
	return 0;
}