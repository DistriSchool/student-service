import { Router } from 'express';
// ...existing code...
// import authMiddleware from '../middlewares/auth'; // Remove or comment out if used here

const router = Router();

// Remove authMiddleware from the routes below if present
// Example before:
// router.get('/', authMiddleware, studentController.list);
// Change to:
router.get('/', studentController.list);
router.get('/:id', studentController.getById);
router.post('/', studentController.create);
router.put('/:id', studentController.update);
router.delete('/:id', studentController.delete);

// ...existing code...

export default router;

