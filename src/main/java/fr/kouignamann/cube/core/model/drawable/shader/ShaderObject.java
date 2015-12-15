package fr.kouignamann.cube.core.model.drawable.shader;

public abstract class ShaderObject {

    private int shaderProgramId;
    private int vertexShaderId;
    private int fragmentShaderId;

    protected ShaderObject() {
        super();
    }

    protected void setup(int shaderProgramId, int vertexShaderId, int fragmentShaderId) {
        this.shaderProgramId = shaderProgramId;
        this.vertexShaderId = vertexShaderId;
        this.fragmentShaderId = fragmentShaderId;
    }

    public int getShaderProgramId() {
        return shaderProgramId;
    }

    public int getVertexShaderId() {
        return vertexShaderId;
    }

    public int getFragmentShaderId() {
        return fragmentShaderId;
    }
}
