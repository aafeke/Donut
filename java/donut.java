// ignore the package error


public class donut {

    final static int screen_height = 40;
    final static int screen_width = 40;
    
    final static float theta_spacing = 0.07f;
    final static float phi_spacing = 0.02f;

    final static int R1 = 1;
    final static int R2 = 2;
    final static int K2 = 5;
    final static int K1  = screen_width * K2 * 3/( 8*(R1+R2) ); 

    static int A = 0;
    static int B = 0;
    static char[][] output = new char[screen_height][screen_width];
    
    public static void _donut() {
        render_frame(A, B);
        A += 0.07;
        B += 0.03;

        System.out.print("\u001B[H");

        for (int j=0; j<screen_height; j++) {
            for(int i=0; i<screen_width; i++) {
                System.out.print(output[i][j]);
            }
            System.out.print("\n");
        }
        return;
    }

    public static void render_frame(float A, float B) {
        final float cosA = (float) Math.cos(A);
        final float sinA = (float) Math.sin(A);
        final float cosB = (float) Math.cos(B);
        final float sinB = (float) Math.sin(B);

        float[][] zbuffer = new float[screen_height][screen_width];

        for (int j=0; j<screen_height; j++) {
            for(int i=0; i<screen_width; i++) {
                output[i][j] = ' ';
            }
        }

        for (int j=0; j<screen_height; j++) {
            for(int i=0; i<screen_width; i++) {
                zbuffer[i][j] = 0;
            }
        }

        for (float theta=0; theta<2*Math.PI; theta+=theta_spacing) {
            final float costheta = (float) Math.cos(theta);
            final float sintheta = (float) Math.sin(theta);

            for (float phi=0; phi<2*Math.PI; phi+=phi_spacing) {
                final float cosphi = (float) Math.cos(phi);
                final float sinphi = (float) Math.sin(phi);

                final float circlex = R2 + R1*costheta;
                final float circley = R1*sintheta;

                final float x = circlex*(cosB*cosphi + sinA*sinB*sinphi) - circley*cosA*sinB;
                final float y = circlex*(sinB*cosphi - sinA*cosB*sinphi) + circley*cosA*cosB;
                final float z = K2 + cosA*circlex*sinphi + circley*sinA;
                final float ooz = 1/z;

                final int xp = (int) Math.floor(screen_width/2 + K1*ooz*x); 
                final int yp = (int) Math.floor(screen_height/2 + K1*ooz*y);

                final float L = cosphi*costheta*sinB - cosA*costheta*sinphi -
                sinA*sintheta + cosB*(cosA*sintheta - costheta*sinA*sinphi); 

                if (L > 0) {
                    if ( ooz > zbuffer[xp][yp] ) {
                        zbuffer[xp][yp] = ooz;
                        final int luminance_index = (int) Math.floor(L*8);
                        final char[] chars = 
                            {'.',',', '-', '~', ':', ';', '=', '!', '*', '#', '$', '@'};
                        output[xp][yp] = chars[luminance_index];
                    }
                }
            }
        }
        return;
    }

    public static void main(String[] args) {
        while(true) {
            _donut();
        }
    }
}