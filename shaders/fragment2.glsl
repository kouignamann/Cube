#version 330 core

uniform sampler2D texture_sampler;

in vec2 pass_TextureCoord;

out vec4 FragColor;

uniform sampler2D scene;
uniform sampler2D bloomBlur;

void main()
{
    const float gamma = 2.2;
    vec3 hdrColor = texture(scene, pass_TextureCoord).rgb;
    vec3 bloomColor = texture(bloomBlur, pass_TextureCoord).rgb;

    hdrColor += bloomColor; // additive blending (bloom = true)

    // tone mapping
    vec3 result = vec3(1.0) - exp(-hdrColor * 1); // exposure = 1
    // also gamma correct while we're at it
    result = pow(result, vec3(1.0 / gamma));
    //FragColor = vec4(result, 1.0f);
    FragColor = vec4(hdrColor, 1.0f);
}
