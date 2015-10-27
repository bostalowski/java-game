package com.japaleno.tools;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class CameraHandler
{
    OrthographicCamera camera;

    public void CameraHandler(float viewportWidth, float viewportHeight)
    {
        this.camera = new OrthographicCamera(viewportWidth, viewportHeight);
    }
}
