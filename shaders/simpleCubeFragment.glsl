#version 330 core

uniform sampler2D texture_sampler;

in vec4 pass_Color;
in vec2 pass_TextureCoord;

out vec4 out_Color;

void main(void) {
    out_Color = pass_Color * texture(texture_sampler, pass_TextureCoord);
}
