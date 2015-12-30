#version 330 core

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

layout (location = 0) in vec4 in_Position;
layout (location = 2) in vec4 in_SelectColor;

out vec4 pass_Color;

void main(void) {
    mat4 modelView = viewMatrix * modelMatrix;
    gl_Position = projectionMatrix * modelView * in_Position;

    pass_Color = in_SelectColor;
}
