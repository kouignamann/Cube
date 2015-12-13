package fr.kouignamann.cube.core.model.drawable;

public class ShaderObject {

    private int shaderProgramId;
    private int vertexShaderId;
    private int fragmentShaderId;

    public ShaderObject(int shaderProgramId, int vertexShaderId, int fragmentShaderId) {
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
