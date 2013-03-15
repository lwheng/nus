#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct Drawable {
    int scaleFactor;
    void (*draw)(struct Drawable *self);
    void (*scaledDraw)(struct Drawable *self, int factor);
};

void drawable_draw(struct Drawable *self) {
    printf("Generic drawable object\n");
}

void drawable_scaledDraw(struct Drawable *self, int factor) {
    self->scaleFactor = factor;
    self->draw(self);
    self->scaleFactor = 1;
}

void init_drawable(struct Drawable *obj) {
    obj->scaleFactor = 1;
    obj->draw = drawable_draw;
    obj->scaledDraw = drawable_scaledDraw;
}

struct Drawable * make_drawable() {
    struct Drawable * retVal = malloc(sizeof(struct Drawable));
    init_drawable(retVal);
    return retVal;
}

struct Circle {
    int scaleFactor;
    void (*draw)(struct Circle *self);
    void (*scaledDraw)(struct Drawable *self, int factor);
    void (*super_draw)(struct Drawable *self);
    int x, y, radius;
};

void circle_draw(struct Circle *self) {
    printf("Circle with center at (%d,%d) and radius %d", self->x, self->y,
                    self->scaleFactor * self->radius);
}

void init_circle(struct Circle *obj, int x, int y, int radius) {
    init_drawable((struct Drawable *)obj);
    obj->x = x;
    obj->y = y;
    obj->radius = radius;
    obj->super_draw = obj->draw;
    obj->draw = circle_draw;
}

struct Circle * make_circle(int x, int y, int radius) {
    struct Circle * retVal = malloc(sizeof(struct Circle));
    init_circle(retVal, x, y, radius);
    return retVal;
}

struct Square {
    int scaleFactor;
    void (*draw)(struct Square *self);
    void (*scaledDraw)(struct Drawable *self, int factor);
    void (*super_draw)(struct Drawable *self);
    int x, y, side;
};

void square_draw(struct Square *self) {
    printf("Square with corner at (%d,%d) and side %d", self->x, self->y,
                    self->scaleFactor * self->side);
}

void init_square(struct Square *obj, int x, int y, int side) {
    init_drawable((struct Drawable *)obj);
    obj->x = x;
    obj->y = y;
    obj->side = side;
    obj->super_draw = obj->draw;
    obj->draw = square_draw;
}

struct Square * make_square(int x, int y, int side) {
    struct Square * retVal = malloc(sizeof(struct Square));
    init_square(retVal, x, y, side);
    return retVal;
}

int main(int argc, char** argv) {
    struct Drawable * x;

    if(strcmp(argv[1], "c") == 0)
        x = make_circle(10, 10, 10);
    else
        x = make_square(5, 5, 5);
    x->scaledDraw(x, 10);
    return 0;
}