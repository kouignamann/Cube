#version 330 core

uniform sampler2D texture_sampler;

in vec4 pass_Color;
in vec2 pass_TextureCoord;

//out vec4 out_Color;
layout (location = 0) out vec4 FragColor;
layout (location = 1) out vec4 BrightColor;

void main(void) {
    FragColor = pass_Color * texture(texture_sampler, pass_TextureCoord);
    float brightness = dot(FragColor.rgb, vec3(0.2126, 0.7152, 0.0722));
    if(brightness > 1.0) {
        BrightColor = vec4(FragColor.rgb, 1.0);
    }
}
