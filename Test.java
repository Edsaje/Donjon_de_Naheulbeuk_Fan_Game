import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class Test {
    public static void main(String[] args) {
        Matrix4 mat = new Matrix4();
        float posX = 0; float posZ = 5f; // y=1
        float half = 2.5f;
        mat.setToTranslation(posX + half, 0, posZ + half);
        mat.rotate(Vector3.Y, 180f);
        mat.translate(0, 0, -half);
        Vector3 pos = new Vector3();
        mat.getTranslation(pos);
        System.out.println("N wall (y=1) -> " + pos.z);

        mat.setToTranslation(posX + half, 0, posZ + half);
        mat.rotate(Vector3.Y, 0f);
        mat.translate(0, 0, -half);
        mat.getTranslation(pos);
        System.out.println("S wall (y=1) -> " + pos.z);
    }
}
